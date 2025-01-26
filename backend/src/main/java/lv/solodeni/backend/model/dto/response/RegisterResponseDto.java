package lv.solodeni.backend.model.dto.response;

import lv.solodeni.backend.model.enums.Status;

public record RegisterResponseDto(
        Status status, String message) {

    public RegisterResponseDto(String message) {
        this(Status.SUCCESS, message);
    }
}
