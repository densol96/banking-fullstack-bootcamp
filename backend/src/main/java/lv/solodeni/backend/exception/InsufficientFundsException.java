package lv.solodeni.backend.exception;

public class InsufficientFundsException extends RuntimeException {
    private String message;

    public InsufficientFundsException(Double balance, Double amount) {
        this.message = "Insufficient funds. Your balance is " + balance + " but you attempted to withdraw " + amount;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
