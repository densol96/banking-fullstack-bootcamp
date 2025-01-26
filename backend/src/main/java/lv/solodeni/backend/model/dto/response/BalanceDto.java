package lv.solodeni.backend.model.dto.response;

import lv.solodeni.backend.model.enums.Status;

public record BalanceDto(
        Status status, Double balance, String message) {

    public BalanceDto(Double balance) {
        this(Status.SUCCESS, balance, "Your balance is " + balance + " now.");
    }
}
