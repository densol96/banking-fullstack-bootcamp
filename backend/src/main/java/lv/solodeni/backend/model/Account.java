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
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import lv.solodeni.backend.exception.InvalidAmountException;

@Entity
@Table(name = "accounts")
@Data
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Setter(value = AccessLevel.NONE)
    @Column(length = 36)
    private UUID id;

    @Column(nullable = false)
    private Double balance = 0.0;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @OneToMany(mappedBy = "fromAccount", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Transaction> sentTransactions;

    @OneToMany(mappedBy = "toAccount", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Transaction> receivedTransactions;

    public Account(Double balance, Customer customer) {
        this.balance = balance;
        this.customer = customer;
    }

    public void deposit(Double amount) throws Exception {
        if (amount < 0)
            throw new InvalidAmountException("Invalid amount of " + amount);
        balance += amount;
    }

    public void withdraw(Double amount) {
        if (amount > balance)
            throw new InvalidAmountException("Not enough funds on this account");
        balance -= amount;
    }
}
