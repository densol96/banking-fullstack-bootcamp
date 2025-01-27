package lv.solodeni.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import lv.solodeni.backend.model.Account;

import java.util.List;
import java.util.Optional;

public interface IAccountRepo extends JpaRepository<Account, Long> {
    Optional<Account> findByAccountNumber(String accountNumber);

    Boolean existsByIdAndCustomerId(Long id, Long customerId);

    List<Account> findAllByCustomerId(Long customerId);

    Integer countAllByCustomerId(Long customerId);
}