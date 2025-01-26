package lv.solodeni.backend.model.dto.response;

import lv.solodeni.backend.model.enums.Status;

public record ErrorDto(
        Status status,
        String message) {
    public ErrorDto(String message) {
        this(Status.FAILURE, message);
    }
}
