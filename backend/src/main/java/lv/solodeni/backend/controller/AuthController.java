package lv.solodeni.backend.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lv.solodeni.backend.exception.FeatureNotAvailableYetException;
import lv.solodeni.backend.model.dto.request.CustomerRegistrationDto;
import lv.solodeni.backend.model.dto.request.SignInCredentialsDto;
import lv.solodeni.backend.model.dto.response.BalanceDto;
import lv.solodeni.backend.model.dto.response.RegisterResponseDto;
import lv.solodeni.backend.model.dto.response.SignInResponseDto;
import lv.solodeni.backend.service.customer.ICustomerService;
import lv.solodeni.backend.service.user.IUserService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final ICustomerService customerService;
    private final IUserService userService;

    @PostMapping("/customers/sign-up")
    public ResponseEntity<RegisterResponseDto> registerCustomer(
            @Valid @RequestBody CustomerRegistrationDto customerData) {
        return new ResponseEntity<>(customerService.register(customerData), HttpStatus.CREATED);
    }

    @PostMapping("/employees/sign-up")
    public String registerEmployee() {
        throw new FeatureNotAvailableYetException("employee registration");
    }

    @PostMapping("/users/sign-in")
    public ResponseEntity<SignInResponseDto> registerEmployee(@Valid @RequestBody SignInCredentialsDto userData) {
        return new ResponseEntity<>(userService.signIn(userData), HttpStatus.OK);
    }

}
