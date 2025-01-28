package lv.solodeni.backend.model.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record SignInCredentialsDto(
        @NotBlank(message = "Email cannot be blank") @Email(message = "Email should be valid") String email,
        @NotBlank(message = "Password cannot be blank or null") @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&])[A-Za-z\\d!@#$%^&]{4,20}$", message = "Password must be 4–20 characters long, include an uppercase letter, a lowercase letter, a number, and a special character.") String password) {

}
