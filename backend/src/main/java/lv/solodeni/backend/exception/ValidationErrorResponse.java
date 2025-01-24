package lv.solodeni.backend.exception;

import java.util.Map;

public record ValidationErrorResponse(String status, Map<String, String> errors) {

    public ValidationErrorResponse(Map<String, String> errors) {
        this("failure", errors);
    }
}