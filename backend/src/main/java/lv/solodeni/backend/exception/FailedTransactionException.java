package lv.solodeni.backend.exception;

public class FailedTransactionException extends RuntimeException {
    public FailedTransactionException() {
        super("Transaction failed");
    }
}
