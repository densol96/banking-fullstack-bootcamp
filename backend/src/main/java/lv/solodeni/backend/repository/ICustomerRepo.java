package lv.solodeni.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import lv.solodeni.backend.model.Customer;

public interface ICustomerRepo extends JpaRepository<Customer, Long> {
    Boolean existsByEmail(String email);
}