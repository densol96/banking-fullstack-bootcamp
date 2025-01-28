package lv.solodeni.backend.service.account;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;

import com.mysql.cj.exceptions.FeatureNotAvailableException;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

import lombok.RequiredArgsConstructor;

import lv.solodeni.backend.exception.DepositLimitExceededException;
import lv.solodeni.backend.exception.FailedTransactionException;
import lv.solodeni.backend.exception.InsufficientFundsException;
import lv.solodeni.backend.exception.InvalidIdException;
import lv.solodeni.backend.exception.InvalidToAcountNumber;
import lv.solodeni.backend.exception.InvalidUserRoleException;
import lv.solodeni.backend.exception.NotClearedBalanceException;
import lv.solodeni.backend.exception.AccountLimitException;
import lv.solodeni.backend.model.Account;
import lv.solodeni.backend.model.Customer;

import lv.solodeni.backend.model.Transaction;
import lv.solodeni.backend.model.User;
import lv.solodeni.backend.model.dto.request.ExternalTransferDto;
import lv.solodeni.backend.model.dto.request.OperationAmountDto;
import lv.solodeni.backend.model.dto.request.TransferDto;
import lv.solodeni.backend.model.dto.response.BalanceDto;
import lv.solodeni.backend.model.dto.response.BasicMessageDto;
import lv.solodeni.backend.model.dto.response.TransactionSucessDto;
import lv.solodeni.backend.model.enums.Status;
import lv.solodeni.backend.model.enums.TransactionType;
import lv.solodeni.backend.repository.IAccountRepo;
import lv.solodeni.backend.repository.ITransactionRepo;
import lv.solodeni.backend.service.user.IUserService;

// employee functionality could be added later, but for now will focus on Bootcamp requirements and write logic as if only customer can do this actions

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements IAccountService {

    private final IAccountRepo accountRepo;
    private final ITransactionRepo transactionRepo;
    private final IUserService userService;
    @PersistenceContext
    private EntityManager entityManager;

    @Value("${bank.maximum_deposit}")
    private Integer maximumDeposit;

    @Value("${bank.code}")
    private String bankCode;

    private Account doSecurityChecksAndExtractAccount(Long accountId) {
        if (accountId < 1)
            throw new InvalidIdException("There is no account with such id of " + accountId);

        User loggedInUser = userService.getLoggedInUser();
        if (!(loggedInUser instanceof Customer)) {
            throw new InvalidUserRoleException("Only customers can view their own balance.");
        }

        if (!accountRepo.existsByIdAndCustomerId(accountId, loggedInUser.getId())) {
            throw new InvalidUserRoleException("Customers cannot view other's banking account balances.");
        }

        return accountRepo.findById(accountId).get();
    }

    @Override
    public BalanceDto displayBalance(Long accountId) {
        Account account = doSecurityChecksAndExtractAccount(accountId);
        Double balance = account.getBalance();
        return new BalanceDto(balance);
    }

    @Override
    @Transactional
    public BalanceDto deposit(Long accountId, OperationAmountDto amountDto) {
        Account account = doSecurityChecksAndExtractAccount(accountId);
        Double amount = amountDto.amount();
        if (amount > maximumDeposit) {
            DepositLimitExceededException e = new DepositLimitExceededException(amount, maximumDeposit);

            transactionRepo
                    .save(new Transaction(null, null, account, amount, Status.FAILURE, TransactionType.DEPOSIT,
                            e.getMessage()));
            throw e;
        }
        account.deposit(amount);
        accountRepo.save(account);
        transactionRepo
                .save(new Transaction(null, null, account, amount, Status.SUCCESS, TransactionType.DEPOSIT, null));
        return new BalanceDto(account.getBalance());
    }

    @Override
    @Transactional
    public BalanceDto withdraw(Long accountId, OperationAmountDto amountDto) {
        Account account = doSecurityChecksAndExtractAccount(accountId);
        Double amount = amountDto.amount();
        Double balance = account.getBalance();
        if (amount > balance) {
            InsufficientFundsException e = new InsufficientFundsException(balance, amount);
            transactionRepo
                    .save(new Transaction(account, null, null, amount, Status.FAILURE, TransactionType.DEPOSIT,
                            e.getMessage()));
            throw e;
        }
        account.withdraw(amount);
        accountRepo.save(account);
        transactionRepo
                .save(new Transaction(account, null, null, amount, Status.SUCCESS, TransactionType.WITHDRAW, null));
        return new BalanceDto(account.getBalance());
    }

    @Override
    @Transactional
    public BalanceDto transfer(Long fromAccountId, TransferDto transferDto) {
        Account fromAccount = doSecurityChecksAndExtractAccount(fromAccountId);
        String toAccountNumber = transferDto.toAccountNumber();

        if (toAccountNumber.startsWith(bankCode)) {
            Account toAccount = null;
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
                        .save(new Transaction(fromAccount, null, toAccount, amount, Status.SUCCESS,
                                TransactionType.TRANSFER,
                                null));
                return new BalanceDto(fromAccount.getBalance());
            } catch (Exception e) {
                transactionRepo
                        .save(new Transaction(fromAccount, null, toAccount, amount, Status.FAILURE,
                                TransactionType.TRANSFER,
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

        if (accountRepo.countAllByCustomerId(loggedInUser.getId()) >= 3)
            throw new AccountLimitException("Customer cannot have more than 3 accounts.");

        Account newAccount = new Account(0.0, (Customer) loggedInUser);
        accountRepo.save(newAccount);
        return new BasicMessageDto("You have successfully created a new account.");
    }

    @Override
    public BasicMessageDto delete(Long accountId) {
        Account accountForDelete = doSecurityChecksAndExtractAccount(accountId);
        if (accountForDelete.getBalance() != 0)
            throw new NotClearedBalanceException();
        accountRepo.delete(accountForDelete);
        return new BasicMessageDto("You have successfully deleted your account.");
    }

    @Override
    @Transactional
    public TransactionSucessDto acceptExternalTransfer(ExternalTransferDto transferDto) {
        /*
         * Response (particularly error) messages could be a bit more specific, but we
         * have decided to keep endpoints simple:
         * "Transaction was successfull" and "Transaction failed"
         * 
         * Also, later I will add the allowed bank identifiers to a DB, but for now,
         * since I do not have them in hand, I will simply use a hard coded list that I
         * will be populating as we go with the bootcamp. And ocne it is complete - move
         * it to a DB
         */
        List<String> knownBankIdentifiers = new ArrayList<>(Arrays.asList("TSTBNK"));

        String fromAccountNumber = transferDto.fromAccountNumber();
        String toAccountNumber = transferDto.toAccountNumber();
        Double amount = transferDto.amount();

        Account targetAccount = accountRepo.findByAccountNumber(toAccountNumber)
                .orElseThrow(() -> new FailedTransactionException());

        // string structure checked on a dto level so its safe to do here
        String fromBankId = fromAccountNumber.split("_")[0];
        if (!knownBankIdentifiers.contains(fromBankId)) {
            transactionRepo
                    .save(new Transaction(null, fromAccountNumber, targetAccount, amount, Status.FAILURE,
                            TransactionType.TRANSFER, "Unknown bank identifier."));
            throw new FailedTransactionException();
        }

        targetAccount.deposit(amount);
        accountRepo.save(targetAccount);
        transactionRepo
                .save(new Transaction(null, fromAccountNumber, targetAccount, amount, Status.SUCCESS,
                        TransactionType.TRANSFER, null));

        return new TransactionSucessDto();
    }

}
