package lv.solodeni.backend.model.dto.response;

import java.time.LocalDateTime;

import lv.solodeni.backend.model.enums.TransactionType;

public record TransactionDto(
                Long id,
                String fromAccountNumber,
                String toAccountNumber,
                Double amount,
                LocalDateTime transactionDateTime,
                TransactionType type) {
}
