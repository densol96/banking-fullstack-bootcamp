package lv.solodeni.backend.service.account;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

import lombok.RequiredArgsConstructor;

import lv.solodeni.backend.exception.DepositLimitExceededException;
import lv.solodeni.backend.exception.InsufficientFundsException;
import lv.solodeni.backend.exception.InvalidIdException;
import lv.solodeni.backend.model.Account;
import lv.solodeni.backend.model.Transaction;
import lv.solodeni.backend.model.dto.BalanceDto;
import lv.solodeni.backend.model.dto.OperationAmountDto;
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

    @Override
    public BalanceDto displayBalance(Long accountId) {
        Account account = accountRepo.findById(accountId)
                .orElseThrow(() -> new InvalidIdException("There is no account with such id of " + accountId));
        Double balance = account.getBalance();
        return new BalanceDto(balance);
    }

    @Transactional
    @Override
    public BalanceDto deposit(Long accountId, OperationAmountDto amountDto) {
        Account account = accountRepo.findById(accountId)
                .orElseThrow(() -> new InvalidIdException("There is no account with such id of " + accountId));
        Double amount = amountDto.amount();
        if (amount > maximumDeposit)
            throw new DepositLimitExceededException(amount, maximumDeposit);
        account.deposit(amount);
        accountRepo.save(account);
        transactionRepo.save(new Transaction(null, account, amount, Status.SUCCESS, TransactionType.DEPOSIT));
        return new BalanceDto(account.getBalance());
    }

    @Transactional
    @Override
    public BalanceDto withdraw(Long accountId, OperationAmountDto amountDto) {
        Account account = accountRepo.findById(accountId)
                .orElseThrow(() -> new InvalidIdException("There is no account with such id of " + accountId));
        Double amount = amountDto.amount();
        Double balance = account.getBalance();
        if (amount > balance)
            throw new InsufficientFundsException(balance, amount);
        account.withdraw(amount);
        accountRepo.save(account);
        transactionRepo.save(new Transaction(account, null, amount, Status.SUCCESS, TransactionType.WITHDRAW));
        return new BalanceDto(account.getBalance());
    }

}
