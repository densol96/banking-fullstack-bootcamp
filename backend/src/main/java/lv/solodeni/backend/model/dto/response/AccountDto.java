package lv.solodeni.backend.model.dto.response;

public record AccountDto(
        Long id,
        Double balance,
        String accountNumber) {
}
