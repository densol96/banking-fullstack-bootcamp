package lv.solodeni.backend.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "bank_whitelist")
@Data
@NoArgsConstructor
public class BankWhitelist {

    @Id
    private String bankId;
    private String urlDomain;

    public BankWhitelist(String bankId, String urlDomain) {
        this.bankId = bankId;
        this.urlDomain = urlDomain;
    }
}