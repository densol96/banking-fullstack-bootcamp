package lv.solodeni.backend.service.customer;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lv.solodeni.backend.exception.EmailAlreadyExistsException;
import lv.solodeni.backend.exception.PasswordsNotMatchException;
import lv.solodeni.backend.model.Customer;
import lv.solodeni.backend.model.dto.request.CustomerRegistrationDto;
import lv.solodeni.backend.model.dto.response.AccountDto;
import lv.solodeni.backend.model.dto.response.CustomerDto;
import lv.solodeni.backend.model.dto.response.RegisterResponseDto;
import lv.solodeni.backend.model.enums.UserRole;
import lv.solodeni.backend.repository.ICustomerRepo;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements ICustomerService {

    private final ICustomerRepo customerRepo;
    private final PasswordEncoder passwordEncoder;

    @Override
    public RegisterResponseDto register(CustomerRegistrationDto customerData) {
        if (!customerData.password().equals(customerData.passwordConfirm()))
            throw new PasswordsNotMatchException();

        if (customerRepo.existsByEmail(customerData.email()))
            throw new EmailAlreadyExistsException();

        Customer newCustomer = Customer.builder()
                .firstName(customerData.firstName())
                .lastName(customerData.lastName())
                .email(customerData.email())
                .password(passwordEncoder.encode(customerData.password()))
                .role(UserRole.CUSTOMER)
                .build();

        customerRepo.save(newCustomer);

        return new RegisterResponseDto("Customer profile created. Check your email ( " + newCustomer.getEmail()
                + " ) for instractions on activation.");
    }

    @Override
    public List<CustomerDto> getAllCustomers() {
        return customerRepo.findAll().stream().map((customer) -> {

            List<AccountDto> accounts = customer.getAccounts().stream()
                    .map(account -> new AccountDto(account.getId(), account.getBalance(), account.getAccountNumber()))
                    .toList();

            CustomerDto json = new CustomerDto(
                    customer.getId(),
                    customer.getEmail(),
                    customer.getFirstName(),
                    customer.getLastName(),
                    accounts);

            return json;
        }).toList();
    }

}
