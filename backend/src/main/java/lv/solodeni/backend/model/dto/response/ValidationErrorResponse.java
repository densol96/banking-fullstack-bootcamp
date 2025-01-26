package lv.solodeni.backend.model.dto.response;

import java.util.Map;

import lv.solodeni.backend.model.enums.Status;

public record ValidationErrorResponse(Status status, Map<String, String> errors) {

    public ValidationErrorResponse(Map<String, String> errors) {
        this(Status.FAILURE, errors);
    }
}