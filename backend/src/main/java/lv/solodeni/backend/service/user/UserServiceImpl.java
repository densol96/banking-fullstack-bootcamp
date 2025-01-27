package lv.solodeni.backend.service.user;

import java.util.List;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import org.hibernate.SessionFactory;
import org.hibernate.Session;

import org.springframework.security.core.userdetails.UserDetails;

import lombok.RequiredArgsConstructor;

import lv.solodeni.backend.exception.EmailNotFoundException;
import lv.solodeni.backend.exception.ServiceUnavailableException;
import lv.solodeni.backend.model.Customer;
import lv.solodeni.backend.model.Employee;
import lv.solodeni.backend.model.User;
import lv.solodeni.backend.model.UserPrincipal;
import lv.solodeni.backend.model.dto.request.SignInCredentialsDto;
import lv.solodeni.backend.model.dto.response.AccountDto;
import lv.solodeni.backend.model.dto.response.SignInResponseDto;
import lv.solodeni.backend.model.dto.response.UserDto;
import lv.solodeni.backend.model.dto.response.UserProfile;
import lv.solodeni.backend.model.enums.UserRole;
import lv.solodeni.backend.repository.IAccountRepo;
import lv.solodeni.backend.repository.IUserRepo;
import lv.solodeni.backend.service.JWTService;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements IUserService {

    private final IUserRepo userRepo;
    private final IAccountRepo accountRepo;
    private final AuthenticationManager authenticationManager;
    private final JWTService jwtService;

    @Override
    public SignInResponseDto signIn(SignInCredentialsDto userData) {
        try {
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            userData.email(),
                            userData.password()));
            String email = ((UserDetails) auth.getPrincipal()).getUsername(); // will refer to an email in this case
            return new SignInResponseDto(jwtService.generateToken(email));
        } catch (AuthenticationException e) {
            System.out.println(e.getClass().getSimpleName() + e);
            throw e;
        }
    }

    @Override
    public UserDto getIdentity() {
        User loggedInUser = getLoggedInUser();
        UserProfile profile;
        boolean isCustomer = loggedInUser.getRole() == UserRole.CUSTOMER;
        if (isCustomer) {
            Customer customer = (Customer) loggedInUser;
            List<AccountDto> accountsAsDto = accountRepo.findAllByCustomerId(customer.getId()).stream()
                    .map(account -> new AccountDto(account.getId(), account.getBalance(),
                            account.getAccountNumber()))
                    .toList();
            profile = new UserProfile(accountsAsDto, null, null);
        } else {
            Employee employee = (Employee) loggedInUser;
            profile = new UserProfile(null, employee.getDepartment(),
                    employee.getPosition());
        }

        return new UserDto(
                loggedInUser.getId(),
                loggedInUser.getEmail(),
                loggedInUser.getFirstName(),
                loggedInUser.getLastName(),
                loggedInUser.getRole(),
                profile);
    }

    public User getLoggedInUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !(auth.getPrincipal() instanceof UserDetails))
            throw new ServiceUnavailableException("Identity service is temporarily not working");
        return ((UserPrincipal) auth.getPrincipal()).getUser();
    }

}
