package lv.solodeni.backend.model.dto.response;

import lv.solodeni.backend.model.enums.Status;

public record BasicMessageDto(
        Status status, String message) {

    public BasicMessageDto(String message) {
        this(Status.SUCCESS, message);
    }
}