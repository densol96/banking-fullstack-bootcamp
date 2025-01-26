package lv.solodeni.backend.model;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import lombok.experimental.SuperBuilder;
import lv.solodeni.backend.exception.NullObjectException;
import lv.solodeni.backend.model.enums.UserRole;

import java.util.*;

@Entity
@Table(name = "customers")
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@SuperBuilder
public class Customer extends User {

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @Builder.Default
    private List<Account> accounts = new ArrayList<>();

    @PrePersist
    @PreUpdate
    private void validateRole() {
        if (getRole() != UserRole.CUSTOMER) {
            throw new IllegalArgumentException("Role must be CUSTOMER for Customer entity.");
        }
    }

    public void addAccount(Account account) throws Exception {
        if (account == null)
            throw new NullObjectException("To add acount it should not be a null");
        accounts.add(account);
    }
}