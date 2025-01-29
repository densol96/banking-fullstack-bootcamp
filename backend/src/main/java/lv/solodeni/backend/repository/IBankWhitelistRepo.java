package lv.solodeni.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import lv.solodeni.backend.model.BankWhitelist;

public interface IBankWhitelistRepo extends JpaRepository<BankWhitelist, String> {

}
