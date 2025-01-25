package lv.solodeni.backend.model;

import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lv.solodeni.backend.model.enums.UserRole;

public class Employee extends User {
    @PrePersist
    @PreUpdate
    private void validateRole() {
        if (getRole() != UserRole.EMPLOYEE) {
            throw new IllegalArgumentException("Role must be EMPLOYEE for Employee entity.");
        }
    }
}
