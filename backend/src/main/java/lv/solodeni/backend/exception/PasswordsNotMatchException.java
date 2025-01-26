package lv.solodeni.backend.exception;

public class PasswordsNotMatchException extends RuntimeException {
    public PasswordsNotMatchException() {
        super("Password and confirmed password do not match.");
    }
}
