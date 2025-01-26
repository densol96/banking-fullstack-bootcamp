package lv.solodeni.backend.exception;

public class PasswordMismatchException extends RuntimeException {

    public PasswordMismatchException() {
        super("Password mismatch.");
    }

    public PasswordMismatchException(String message) {
        super(message);
    }
}
