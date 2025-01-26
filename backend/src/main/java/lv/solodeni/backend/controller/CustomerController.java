package lv.solodeni.backend.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lv.solodeni.backend.model.dto.response.CustomerDto;
import lv.solodeni.backend.service.customer.ICustomerService;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/api/v1/customers")
@RequiredArgsConstructor
public class CustomerController {
    public final ICustomerService customerService;

    @GetMapping("/all")
    public ResponseEntity<List<CustomerDto>> getMethodName() {
        return new ResponseEntity<>(customerService.getAllCustomers(), HttpStatus.OK);
    }

}
