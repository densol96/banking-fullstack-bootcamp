package lv.solodeni.backend.model.dto.response;

public record SignInResponseDto(
        String message,
        String jwt) {
    public SignInResponseDto(String jwt) {
        this("Login successful", jwt);
    }
}
