package lv.solodeni.backend.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import lv.solodeni.backend.model.enums.UserRole;

@Entity
@Table(name = "employees")
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@SuperBuilder
public class Employee extends User {

    @Column(nullable = false)
    private String department;

    @Column(nullable = false)
    private String position;

    @PrePersist
    @PreUpdate
    private void validateRole() {
        if (getRole() != UserRole.EMPLOYEE) {
            throw new IllegalArgumentException("Role must be EMPLOYEE for Employee entity.");
        }
    }
}
