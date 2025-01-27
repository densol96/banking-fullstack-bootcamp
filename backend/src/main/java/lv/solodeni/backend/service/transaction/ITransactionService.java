package lv.solodeni.backend.service.transaction;

import java.util.List;

import lv.solodeni.backend.model.dto.response.TransactionDto;

public interface ITransactionService {
    List<TransactionDto> getLoggedInUserTransactions(Long accountId);
}
