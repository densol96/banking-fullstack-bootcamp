package lv.solodeni.backend.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import lv.solodeni.backend.model.Account;

public interface IAccountRepo extends JpaRepository<Account, UUID> {
}