package lv.solodeni.backend.model.dto;

public record BalanceDto(
        String status, Double balance, String message) {

    public BalanceDto(Double balance) {
        this("success", balance, "Your balance is " + balance + " now.");
    }
}
