package lv.solodeni.backend.model;

import java.util.List;
import java.util.UUID;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "accounts")
@Data
@NoArgsConstructor
public class Account {

    @Transient
    private static String BANK_PREFIX = "SOLDEN_";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(value = AccessLevel.NONE)
    private Long id;

    @Column(nullable = false)
    private Double balance = 0.0;

    @Column(nullable = false, unique = true)
    private String accountNumber;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @OneToMany(mappedBy = "fromAccount", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Transaction> sentTransactions;

    @OneToMany(mappedBy = "toAccount", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Transaction> receivedTransactions;

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Transaction> operationHistory;

    public Account(Double balance, Customer customer) {
        this.balance = balance;
        this.customer = customer;
        this.accountNumber = generateAccountNumber();
    }

    // Validation logic is in a service layer
    public void deposit(Double amount) {
        balance += amount;
    }

    public void withdraw(Double amount) {
        balance -= amount;
    }

    private String generateAccountNumber() {
        return BANK_PREFIX + UUID.randomUUID().toString().replaceAll("-", "").substring(0, 12);
    }
}
