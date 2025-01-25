package lv.solodeni.backend.model.dto;

import java.util.Map;

public record ValidationErrorResponse(String status, Map<String, String> errors) {

    public ValidationErrorResponse(Map<String, String> errors) {
        this("failure", errors);
    }
}