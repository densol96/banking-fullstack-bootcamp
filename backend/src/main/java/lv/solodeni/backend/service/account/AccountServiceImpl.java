package lv.solodeni.backend.service.account;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.mysql.cj.exceptions.FeatureNotAvailableException;

import jakarta.transaction.Transactional;

import lombok.RequiredArgsConstructor;

import lv.solodeni.backend.exception.DepositLimitExceededException;
import lv.solodeni.backend.exception.InsufficientFundsException;
import lv.solodeni.backend.exception.InvalidIdException;
import lv.solodeni.backend.exception.InvalidToAcountNumber;
import lv.solodeni.backend.exception.InvalidUserRoleException;
import lv.solodeni.backend.exception.AccountLimitException;
import lv.solodeni.backend.model.Account;
import lv.solodeni.backend.model.Customer;
import lv.solodeni.backend.model.Employee;
import lv.solodeni.backend.model.Transaction;
import lv.solodeni.backend.model.User;
import lv.solodeni.backend.model.dto.request.OperationAmountDto;
import lv.solodeni.backend.model.dto.request.TransferDto;
import lv.solodeni.backend.model.dto.response.BalanceDto;
import lv.solodeni.backend.model.dto.response.BasicMessageDto;
import lv.solodeni.backend.model.enums.Status;
import lv.solodeni.backend.model.enums.TransactionType;
import lv.solodeni.backend.repository.IAccountRepo;
import lv.solodeni.backend.repository.ITransactionRepo;
import lv.solodeni.backend.service.user.IUserService;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements IAccountService {

    private final IAccountRepo accountRepo;
    private final ITransactionRepo transactionRepo;
    private final IUserService userService;

    @Value("${bank.maximum_deposit}")
    private Integer maximumDeposit;

    @Value("${bank.code}")
    private String bankCode;

    @Override
    public BalanceDto displayBalance(Long accountId) {

        if (accountId < 1)
            throw new InvalidIdException("There is no account with such id of " + accountId);

        Account account = accountRepo.findById(accountId)
                .orElseThrow(() -> new InvalidIdException("There is no account with such id of " + accountId));
        Double balance = account.getBalance();
        return new BalanceDto(balance);
    }

    @Transactional
    @Override
    public BalanceDto deposit(Long accountId, OperationAmountDto amountDto) {
        if (accountId < 1)
            throw new InvalidIdException("There is no account with such id of " + accountId);

        Account account = accountRepo.findById(accountId)
                .orElseThrow(() -> new InvalidIdException("There is no account with such id of " + accountId));
        Double amount = amountDto.amount();
        if (amount > maximumDeposit) {
            DepositLimitExceededException e = new DepositLimitExceededException(amount, maximumDeposit);
            transactionRepo
                    .save(new Transaction(null, account, amount, Status.FAILURE, TransactionType.DEPOSIT,
                            e.getMessage()));
            throw e;
        }
        account.deposit(amount);
        accountRepo.save(account);
        transactionRepo.save(new Transaction(null, account, amount, Status.SUCCESS, TransactionType.DEPOSIT, null));
        return new BalanceDto(account.getBalance());
    }

    @Transactional
    @Override
    public BalanceDto withdraw(Long accountId, OperationAmountDto amountDto) {

        if (accountId < 1)
            throw new InvalidIdException("There is no account with such id of " + accountId);

        Account account = accountRepo.findById(accountId)
                .orElseThrow(() -> new InvalidIdException("There is no account with such id of " + accountId));
        Double amount = amountDto.amount();
        Double balance = account.getBalance();
        if (amount > balance) {
            InsufficientFundsException e = new InsufficientFundsException(balance, amount);
            transactionRepo
                    .save(new Transaction(account, null, amount, Status.FAILURE, TransactionType.DEPOSIT,
                            e.getMessage()));
            throw e;
        }
        account.withdraw(amount);
        accountRepo.save(account);
        transactionRepo.save(new Transaction(account, null, amount, Status.SUCCESS, TransactionType.WITHDRAW, null));
        return new BalanceDto(account.getBalance());
    }

    @Transactional
    @Override
    public BalanceDto transfer(Long fromAccountId, TransferDto transferDto) {

        if (fromAccountId < 1)
            throw new InvalidIdException("There is no fromAccountId with such id of " + fromAccountId);

        User loggedInUser = userService.getLoggedInUser();

        if (!(loggedInUser instanceof Customer))
            throw new InvalidUserRoleException("Only customers can transfer money");

        Customer customer = (Customer) loggedInUser;

        boolean belongsToLoggedInUser = false;
        Account fromAccount = null;

        for (Account acc : customer.getAccounts()) {
            if (acc.getId() == fromAccountId) {
                belongsToLoggedInUser = true;
                fromAccount = acc;
                break;
            }
        }

        if (!belongsToLoggedInUser)
            throw new InvalidUserRoleException("Customers can only transfer money from their accounts");

        String toAccountNumber = transferDto.toAccountNumber();
        Account toAccount = null;
        if (toAccountNumber.startsWith(bankCode)) {

            Double balance = fromAccount.getBalance();
            Double amount = transferDto.amount();
            try {
                if (fromAccount.getAccountNumber().equals(toAccountNumber))
                    throw new InvalidIdException("Transfers to the same account (to yourself) is not allowed");

                if (balance < amount)
                    throw new InsufficientFundsException(balance, amount);

                toAccount = accountRepo.findByAccountNumber(toAccountNumber)
                        .orElseThrow(() -> new InvalidToAcountNumber(
                                "There is no account with such toAccountNumber of " + toAccountNumber));
                fromAccount.withdraw(amount);
                toAccount.deposit(amount);
                accountRepo.saveAll(Arrays.asList(fromAccount, toAccount));
                transactionRepo
                        .save(new Transaction(fromAccount, toAccount, amount, Status.SUCCESS, TransactionType.TRANSFER,
                                null));
                return new BalanceDto(fromAccount.getBalance());
            } catch (Exception e) {
                transactionRepo
                        .save(new Transaction(fromAccount, toAccount, amount, Status.FAILURE, TransactionType.TRANSFER,
                                null));
                throw e;
            }
        } else {
            throw new FeatureNotAvailableException("Transfers to another banks feature is being developed...");
        }
    }

    @Override
    public BasicMessageDto create() {
        User loggedInUser = userService.getLoggedInUser();
        if (!(loggedInUser instanceof Customer))
            throw new InvalidUserRoleException("Only customers can create new account");
        Customer customer = (Customer) loggedInUser;
        if (customer.getAccounts().size() >= 3)
            throw new AccountLimitException("Customer cannot have more than 3 accounts.");
        Account newAccount = new Account(0.0, customer);
        accountRepo.save(newAccount);
        return new BasicMessageDto("You have successfully created a new account.");
    }

    @Override
    public BasicMessageDto delete(Long accountId) {
        User loggedInUser = userService.getLoggedInUser();
        if (!(loggedInUser instanceof Customer))
            throw new InvalidUserRoleException("Only customers can delete a banking account.");
        Customer customer = (Customer) loggedInUser;
        if (customer.getAccounts() == null || customer.getAccounts().size() == 0)
            throw new AccountLimitException("You do not have any banking accounts.");

        boolean belongsToLoggedInUser = false;
        for (Account acc : customer.getAccounts()) {
            if (acc.getId() == accountId) {
                belongsToLoggedInUser = true;
                break;
            }
        }
        if (!belongsToLoggedInUser)
            throw new InvalidUserRoleException("Customers can only delete their own accounts");

        accountRepo.deleteById(accountId);
        return new BasicMessageDto("You have successfully deleted your account.");

    }

}
