package lv.solodeni.backend.service.customer;

import java.util.List;

import lv.solodeni.backend.model.dto.request.CustomerRegistrationDto;

import lv.solodeni.backend.model.dto.response.CustomerDto;
import lv.solodeni.backend.model.dto.response.RegisterResponseDto;

public interface ICustomerService {
    RegisterResponseDto register(CustomerRegistrationDto customerData);

    List<CustomerDto> getAllCustomers();
}
