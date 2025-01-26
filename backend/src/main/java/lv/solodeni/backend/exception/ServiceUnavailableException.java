package lv.solodeni.backend.exception;

public class ServiceUnavailableException extends RuntimeException {
    public ServiceUnavailableException() {
        super("Service is currently unavailable.Please try again later!");
    }

    public ServiceUnavailableException(String message) {
        super(message);
    }
}
