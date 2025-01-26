package lv.solodeni.backend.service.user;

import lv.solodeni.backend.model.dto.request.SignInCredentialsDto;
import lv.solodeni.backend.model.dto.response.SignInResponseDto;

public interface IUserService {
    SignInResponseDto signIn(SignInCredentialsDto userData);
}
