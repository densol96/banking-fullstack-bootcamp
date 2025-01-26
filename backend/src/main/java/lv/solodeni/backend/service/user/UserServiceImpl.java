package lv.solodeni.backend.service.user;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.RequiredArgsConstructor;

import lv.solodeni.backend.exception.EmailNotFoundException;
import lv.solodeni.backend.model.User;
import lv.solodeni.backend.model.UserPrincipal;
import lv.solodeni.backend.model.dto.request.SignInCredentialsDto;
import lv.solodeni.backend.model.dto.response.SignInResponseDto;
import lv.solodeni.backend.repository.IUserRepo;
import lv.solodeni.backend.service.JWTService;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements IUserService {

    private final IUserRepo userRepo;
    private final AuthenticationManager authenticationManager;
    private final JWTService jwtService;

    @Override
    public SignInResponseDto signIn(SignInCredentialsDto userData) {
        User user = userRepo.findByEmail(userData.email()).orElseThrow(() -> new EmailNotFoundException());
        try {
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            userData.email(),
                            userData.password()));
            String email = ((UserDetails) auth.getPrincipal()).getUsername(); // will refer to an email in this case
            return new SignInResponseDto(jwtService.generateToken(email));
        } catch (AuthenticationException e) {
            System.out.println(e.getClass().getSimpleName() + e);
            return null;
        }
    }

}
