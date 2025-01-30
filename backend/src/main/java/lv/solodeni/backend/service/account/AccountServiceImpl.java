package lv.solodeni.backend.service.account;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

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
import lv.solodeni.backend.model.BankWhitelist;
import lv.solodeni.backend.model.Customer;

import lv.solodeni.backend.model.Transaction;
import lv.solodeni.backend.model.User;
import lv.solodeni.backend.model.dto.request.ExternalTransferDto;
import lv.solodeni.backend.model.dto.request.OperationAmountDto;
import lv.solodeni.backend.model.dto.request.TransferDto;
import lv.solodeni.backend.model.dto.response.BalanceDto;
import lv.solodeni.backend.model.dto.response.BasicMessageDto;
import lv.solodeni.backend.model.dto.response.ExternalAccountDto;
import lv.solodeni.backend.model.dto.response.TransactionSucessDto;

import lv.solodeni.backend.model.enums.TransactionType;
import lv.solodeni.backend.repository.IAccountRepo;
import lv.solodeni.backend.repository.IBankWhitelistRepo;
import lv.solodeni.backend.repository.ITransactionRepo;
import lv.solodeni.backend.service.user.IUserService;

// employee functionality could be added later, but for now will focus on Bootcamp requirements and write logic as if only customer can do this actions

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements IAccountService {

    private final IAccountRepo accountRepo;
    private final ITransactionRepo transactionRepo;
    private final IUserService userService;
    private final IBankWhitelistRepo whitelistRepo;

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
            throw new InvalidUserRoleException("You are not a customer type of user to eprform this action.");
        }

        if (!accountRepo.existsByIdAndCustomerId(accountId, loggedInUser.getId())) {
            throw new InvalidUserRoleException("Customers can only have manage the bank accounts that belong to them.");
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
        if (amount > maximumDeposit)
            throw new DepositLimitExceededException(amount, maximumDeposit);
        account.deposit(amount);
        accountRepo.save(account);
        transactionRepo.save(
                Transaction.builder()
                        .toAccount(account)
                        .amount(amount)
                        .type(TransactionType.DEPOSIT)
                        .build());

        return new BalanceDto(account.getBalance());
    }

    @Override
    @Transactional
    public BalanceDto withdraw(Long accountId, OperationAmountDto amountDto) {
        Account account = doSecurityChecksAndExtractAccount(accountId);
        Double amount = amountDto.amount();
        Double balance = account.getBalance();
        if (amount > balance)
            throw new InsufficientFundsException(balance, amount);
        account.withdraw(amount);
        accountRepo.save(account);
        transactionRepo.save(
                Transaction.builder()
                        .fromAccount(account)
                        .amount(amount)
                        .type(TransactionType.WITHDRAW)
                        .build());
        return new BalanceDto(account.getBalance());
    }

    @Override
    @Transactional
    public BalanceDto transfer(Long fromAccountId, TransferDto transferDto) {
        Account fromAccount = doSecurityChecksAndExtractAccount(fromAccountId);
        String toAccountNumber = transferDto.toAccountNumber();
        Double balance = fromAccount.getBalance();
        Double amount = transferDto.amount();

        if (balance < amount)
            throw new InsufficientFundsException(balance, amount);

        String bankId = toAccountNumber.split("_")[0];
        BankWhitelist whiteListBankRecord = whitelistRepo.findById(bankId)
                .orElseThrow(() -> new InvalidIdException("Unknown bank identifier of " + bankId));

        if (whiteListBankRecord.getBankId().equals(bankCode)) {
            Account toAccount = null;

            if (fromAccount.getAccountNumber().equals(toAccountNumber))
                throw new InvalidIdException("Transfers to the same account (to yourself) is not allowed");

            toAccount = accountRepo.findByAccountNumber(toAccountNumber)
                    .orElseThrow(() -> new InvalidToAcountNumber(
                            "There is no account with such toAccountNumber of " + toAccountNumber));

            fromAccount.withdraw(amount);
            toAccount.deposit(amount);
            accountRepo.saveAll(Arrays.asList(fromAccount, toAccount));

            transactionRepo.save(Transaction.builder()
                    .fromAccount(fromAccount)
                    .toAccount(toAccount)
                    .amount(amount)
                    .type(TransactionType.TRANSFER)
                    .build());
            return new BalanceDto(fromAccount.getBalance());
        } else {
            if (makeExternalTransfer(fromAccount.getAccountNumber(), toAccountNumber, amount,
                    whiteListBankRecord.getUrlDomain())) {

                fromAccount.withdraw(amount);
                accountRepo.save(fromAccount);
                transactionRepo.save(Transaction.builder()
                        .fromAccount(fromAccount)
                        .externalToAccountNumber(toAccountNumber)
                        .amount(amount)
                        .type(TransactionType.TRANSFER)
                        .build());
                return new BalanceDto(fromAccount.getBalance());
            } else {
                throw new FailedTransactionException();
            }
        }
    }

    private boolean makeExternalTransfer(String fromAccountNumber, String toAccountNumber, Double amount,
            String domainName) {
        RestTemplate restTemplate = new RestTemplate();
        String url = domainName + "/api/v1/accounts/transfer/external";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("fromAccountNumber", fromAccountNumber);
        requestBody.put("toAccountNumber", toAccountNumber);
        requestBody.put("amount", amount);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);

        try {
            ResponseEntity<JsonNode> response = restTemplate.postForEntity(url, request,
                    JsonNode.class);

            System.out.println("RESPONSE FROM THE EXTERNAL SYSTEM: "
                    + response.getBody().get("message").asText());

            return true;
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            System.out.println("transfer-external === Http* Error => " + e.getMessage());
            throw new FailedTransactionException();
        } catch (Exception e) {
            System.out.println("transfer-external === UNEXPECTED ERROR");
            throw new FailedTransactionException();
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
         */

        String fromAccountNumber = transferDto.fromAccountNumber();
        String toAccountNumber = transferDto.toAccountNumber();
        Double amount = transferDto.amount();

        // string structure checked on a dto level so its safe to do here
        String fromBankId = fromAccountNumber.split("_")[0];

        if (!whitelistRepo.existsById(fromBankId))
            throw new InvalidIdException("Unknown bank identifier of " + fromBankId);

        Account targetAccount = accountRepo.findByAccountNumber(toAccountNumber)
                .orElseThrow(() -> new FailedTransactionException());

        targetAccount.deposit(amount);
        accountRepo.save(targetAccount);
        transactionRepo.save(
                Transaction.builder()
                        .externalFromAccountNumber(fromAccountNumber)
                        .toAccount(targetAccount)
                        .amount(amount)
                        .type(TransactionType.TRANSFER)
                        .build());
        return new TransactionSucessDto();
    }

    @Override
    public List<ExternalAccountDto> displayPublicBankAccounts() {
        return accountRepo.findAll().stream()
                .map(acc -> new ExternalAccountDto(acc.getAccountNumber(), acc.getBalance())).toList();
    }

}
