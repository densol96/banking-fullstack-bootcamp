package lv.solodeni.backend.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;

public record ExternalTransferDto(
                @NotBlank(message = "Account number cannot be blank") @Pattern(regexp = "[A-Z]{6}_[a-z0-9]{12}", message = "Invalid format, must match: [A-Z]{6}_[a-z0-9]{12}") String fromAccountNumber,
                @NotBlank(message = "Account number cannot be blank") @Pattern(regexp = "[A-Z]{6}_[a-z0-9]{12}", message = "Invalid format, must match: [A-Z]{6}_[a-z0-9]{12}") String toAccountNumber,
                @Positive(message = "Amount must be greater than 0") @NotNull(message = "Amount cannot be null") Double amount) {
}
