package lv.solodeni.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import lv.solodeni.backend.model.Account;
import lv.solodeni.backend.model.Transaction;

public interface ITransactionRepo extends JpaRepository<Transaction, Long> {
    @Query("SELECT t FROM Transaction t WHERE t.fromAccount = :account OR t.toAccount = :account")
    List<Transaction> findAllByFromAccountOrToAccount(@Param("account") Account account);

}