package lv.solodeni.backend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import lv.solodeni.backend.model.User;

public interface IUserRepo extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}
