package lv.solodeni.backend.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record TransferDto(
        @Positive(message = "fromAccountId must be greater than 0") @NotNull(message = "fromAccountId cannot be null") Long fromAccountId,
        @NotBlank @Size(min = 10, max = 10, message = "toAccountNumber must be exactly 10 characters") String toAccountNumber,
        @Positive(message = "Amount must be greater than 0") @NotNull(message = "Amount cannot be null") Double amount) {
}
