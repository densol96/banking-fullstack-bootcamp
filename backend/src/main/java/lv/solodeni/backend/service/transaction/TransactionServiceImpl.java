package lv.solodeni.backend.service.transaction;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lv.solodeni.backend.exception.InvalidUserRoleException;
import lv.solodeni.backend.model.Account;
import lv.solodeni.backend.model.Customer;
import lv.solodeni.backend.model.Employee;

import lv.solodeni.backend.model.User;
import lv.solodeni.backend.model.dto.response.TransactionDto;
import lv.solodeni.backend.repository.ITransactionRepo;
import lv.solodeni.backend.service.user.IUserService;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements ITransactionService {

    private final IUserService userService;
    private final ITransactionRepo transactionRepo;

    @Override
    public List<TransactionDto> getLoggedInUserTransactions(Long accountId) {
        User loggedInUser = userService.getLoggedInUser();

        if (loggedInUser instanceof Employee)
            throw new InvalidUserRoleException("Only customers have banking accounts");

        Customer customer = (Customer) loggedInUser;

        boolean belongsToLoggedInUser = false;
        Account fromAccount = null;

        for (Account acc : customer.getAccounts()) {
            if (acc.getId() == accountId) {
                belongsToLoggedInUser = true;
                fromAccount = acc;
                break;
            }
        }

        if (!belongsToLoggedInUser)
            throw new InvalidUserRoleException("Customers can only require their own transactions history");

        return transactionRepo.findAllByFromAccountOrToAccount(fromAccount).stream()
                .map(transaction -> new TransactionDto(
                        transaction.getId(),
                        transaction.getFromAccount() != null ? transaction.getFromAccount().getAccountNumber() : null,
                        transaction.getToAccount() != null ? transaction.getToAccount().getAccountNumber() : null,
                        transaction.getAmount(),
                        transaction.getTransactionDateTime(),
                        transaction.getStatus(),
                        transaction.getErrorMessage(),
                        transaction.getType()))
                .toList();
    }
}
