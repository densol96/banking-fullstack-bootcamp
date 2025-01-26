package lv.solodeni.backend.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record TransferDto(
        @NotBlank @Size(min = 19, max = 19, message = "toAccountNumber must be exactly 18 characters") String toAccountNumber,
        @Positive(message = "Amount must be greater than 0") @NotNull(message = "Amount cannot be null") Double amount) {
}
