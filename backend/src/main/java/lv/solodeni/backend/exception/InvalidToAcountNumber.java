package lv.solodeni.backend.exception;

public class InvalidToAcountNumber extends RuntimeException {
    public InvalidToAcountNumber(String message) {
        super(message);
    }
}