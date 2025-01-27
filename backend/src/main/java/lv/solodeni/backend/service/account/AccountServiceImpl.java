package lv.solodeni.backend.service.account;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

import lombok.RequiredArgsConstructor;

import lv.solodeni.backend.exception.DepositLimitExceededException;
import lv.solodeni.backend.exception.InsufficientFundsException;
import lv.solodeni.backend.exception.InvalidIdException;
import lv.solodeni.backend.exception.InvalidToAcountNumber;
import lv.solodeni.backend.model.Account;
import lv.solodeni.backend.model.Transaction;
import lv.solodeni.backend.model.dto.request.OperationAmountDto;
import lv.solodeni.backend.model.dto.request.TransferDto;
import lv.solodeni.backend.model.dto.response.BalanceDto;
import lv.solodeni.backend.model.enums.Status;
import lv.solodeni.backend.model.enums.TransactionType;
import lv.solodeni.backend.repository.IAccountRepo;
import lv.solodeni.backend.repository.ITransactionRepo;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements IAccountService {

    private final IAccountRepo accountRepo;
    private final ITransactionRepo transactionRepo;

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

        String toAccountNumber = transferDto.toAccountNumber();
        Account toAccount = null;
        if (toAccountNumber.startsWith(bankCode)) {
            Account fromAccount = accountRepo.findById(fromAccountId)
                    .orElseThrow(() -> new InvalidIdException("There is no account with such id of " + fromAccountId));

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
            return new BalanceDto(Status.FAILURE, null, "Transfers to other bankings systems is not available yet");
        }
    }

}
