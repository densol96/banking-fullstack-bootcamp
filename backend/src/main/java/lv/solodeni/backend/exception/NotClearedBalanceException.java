package lv.solodeni.backend.exception;

public class NotClearedBalanceException extends RuntimeException {
    public NotClearedBalanceException() {
        super("You have still an uncleared balance");
    }
}
