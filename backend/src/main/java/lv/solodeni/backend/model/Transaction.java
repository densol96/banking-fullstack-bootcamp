package lv.solodeni.backend.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

import lv.solodeni.backend.model.enums.Status;
import lv.solodeni.backend.model.enums.TransactionType;

// Deposit: 
// - receiverAccount will be filled (the account where the money is deposited)
// - senderAccount may be null or represent an external source

// Withdrawal:
// - senderAccount will be filled (the account from which the money is withdrawn)
// - receiverAccount will generally be null

// Transfer:
// - both senderAccount and receiverAccount will be filled (representing the transfer of money between two accounts)

@Entity
@Table(name = "transactions")
@Data
@NoArgsConstructor
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(value = AccessLevel.NONE)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "from_account_id")
    private Account fromAccount;

    @ManyToOne
    @JoinColumn(name = "to_account_id")
    private Account toAccount;

    @Column(nullable = false)
    private Double amount;

    @Column(nullable = false)
    private LocalDateTime transactionDateTime;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionType type;

    public Transaction(Account fromAccount, Account toAccount, Double amount, Status status, TransactionType type) {
        this.fromAccount = fromAccount;
        this.toAccount = toAccount;
        this.amount = amount;
        this.transactionDateTime = LocalDateTime.now();
        this.status = status;
        this.type = type;
    }

}