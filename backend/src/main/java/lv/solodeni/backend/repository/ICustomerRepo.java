package lv.solodeni.backend.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import lv.solodeni.backend.model.Customer;

public interface ICustomerRepo extends JpaRepository<Customer, UUID> {
}