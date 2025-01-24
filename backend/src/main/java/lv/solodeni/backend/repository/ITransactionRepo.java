package lv.solodeni.backend.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import lv.solodeni.backend.model.Transaction;

public interface ITransactionRepo extends JpaRepository<Transaction, UUID> {
}