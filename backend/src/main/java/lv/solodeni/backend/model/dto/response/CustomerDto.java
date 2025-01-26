package lv.solodeni.backend.model.dto.response;

import java.util.List;

public record CustomerDto(
        Long id,
        String email,
        String firstName,
        String lastName,
        List<AccountDto> accounts) {
}
