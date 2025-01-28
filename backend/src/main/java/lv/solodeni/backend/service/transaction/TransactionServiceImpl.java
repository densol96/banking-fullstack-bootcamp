package lv.solodeni.backend.service.transaction;

import java.util.List;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lv.solodeni.backend.exception.InvalidUserRoleException;
import lv.solodeni.backend.model.Account;
import lv.solodeni.backend.model.Customer;

import lv.solodeni.backend.model.User;
import lv.solodeni.backend.model.dto.response.TransactionDto;
import lv.solodeni.backend.repository.IAccountRepo;
import lv.solodeni.backend.repository.ITransactionRepo;
import lv.solodeni.backend.service.user.IUserService;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements ITransactionService {

    private final IUserService userService;
    private final ITransactionRepo transactionRepo;
    private final IAccountRepo accountRepo;

    @Override
    @Transactional
    public List<TransactionDto> getLoggedInUserTransactions(Long accountId) {
        User loggedInUser = userService.getLoggedInUser();

        if (!(loggedInUser instanceof Customer))
            throw new InvalidUserRoleException("Only customers have banking accounts");

        if (!accountRepo.existsByIdAndCustomerId(accountId, loggedInUser.getId())) {
            throw new InvalidUserRoleException("Customers cannot delete other's banking account.");
        }
        Account requestedAccount = accountRepo.findById(accountId).get();

        return transactionRepo.findAllByFromAccountOrToAccount(requestedAccount).stream()
                .map(transaction -> new TransactionDto(
                        transaction.getId(),
                        transaction.getFromAccount() != null ? transaction.getFromAccount().getAccountNumber()
                                : transaction.getExternalFromAccountNumber(), // could also be null if withdrawal, but
                                                                              // this ternary will work
                        transaction.getToAccount() != null ? transaction.getToAccount().getAccountNumber() : null,
                        transaction.getAmount(),
                        transaction.getTransactionDateTime(),
                        transaction.getStatus(),
                        transaction.getErrorMessage(),
                        transaction.getType()))
                .toList();
    }
}
