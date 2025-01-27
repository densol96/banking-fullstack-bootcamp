package lv.solodeni.backend.exception;

public class MaximumAccountLimitException extends RuntimeException {
    public MaximumAccountLimitException(String message) {
        super(message);
    }
}
