package lv.solodeni.backend.model.dto.response;

import java.util.List;

public record UserProfile(List<AccountDto> accounts, String department, String position) {
}
