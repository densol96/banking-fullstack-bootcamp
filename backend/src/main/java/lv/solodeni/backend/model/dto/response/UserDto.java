package lv.solodeni.backend.model.dto.response;

import lv.solodeni.backend.model.enums.UserRole;

public record UserDto(
        Long id,
        String email,
        String firstName,
        String lastName,
        UserRole role,
        UserProfile profile) {
}
