package lv.solodeni.backend.model.dto.response;

public record TransactionErrorDto(
        String message) {
    public TransactionErrorDto() {
        this("Transaction failed");
    }
}
