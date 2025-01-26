package lv.solodeni.backend.model.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record OperationAmountDto(
        @Positive(message = "Amount must be greater than 0") @NotNull(message = "Amount cannot be null") Double amount) {
}
