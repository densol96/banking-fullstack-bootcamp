package lv.solodeni.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import lv.solodeni.backend.model.Transaction;

public interface ITransactionRepo extends JpaRepository<Transaction, Long> {
}