package lv.solodeni.backend.service.user;

import lv.solodeni.backend.model.User;
import lv.solodeni.backend.model.dto.request.SignInCredentialsDto;
import lv.solodeni.backend.model.dto.response.SignInResponseDto;
import lv.solodeni.backend.model.dto.response.UserDto;

public interface IUserService {
    SignInResponseDto signIn(SignInCredentialsDto userData);

    UserDto getIdentity();

    User getLoggedInUser();
}
