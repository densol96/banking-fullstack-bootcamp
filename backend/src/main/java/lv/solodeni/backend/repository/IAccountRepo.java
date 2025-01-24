package lv.solodeni.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import lv.solodeni.backend.model.Account;

public interface IAccountRepo extends JpaRepository<Account, Long> {
}