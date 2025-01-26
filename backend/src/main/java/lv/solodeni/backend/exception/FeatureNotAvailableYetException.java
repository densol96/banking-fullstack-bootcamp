package lv.solodeni.backend.exception;

public class FeatureNotAvailableYetException extends RuntimeException {
    public FeatureNotAvailableYetException(String featureName) {
        super("This feature of " + featureName + " is not avaialable yet.");
    }
}
