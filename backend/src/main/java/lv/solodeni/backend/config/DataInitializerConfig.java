package lv.solodeni.backend.config;

import java.util.Arrays;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;

import lv.solodeni.backend.model.Account;
import lv.solodeni.backend.model.BankWhitelist;
import lv.solodeni.backend.model.Customer;
import lv.solodeni.backend.model.Transaction;
import lv.solodeni.backend.model.enums.TransactionType;
import lv.solodeni.backend.model.enums.UserRole;
import lv.solodeni.backend.repository.IBankWhitelistRepo;
import lv.solodeni.backend.repository.ICustomerRepo;
import lv.solodeni.backend.repository.ITransactionRepo;

@Configuration
public class DataInitializerConfig {
        @Bean
        @Profile("dev_2")
        public CommandLineRunner seedInitialDataInDatabase(ICustomerRepo customerRepo, ITransactionRepo transactionRepo,
                        PasswordEncoder encoder) {
                return args -> {
                        Customer customer1 = Customer.builder()
                                        .firstName("Deniss")
                                        .lastName("Solovjovs")
                                        .email("solo@deni.com")
                                        .password(encoder.encode("Password123!"))
                                        .role(UserRole.CUSTOMER)
                                        .build();

                        Customer customer2 = Customer.builder()
                                        .firstName("New")
                                        .lastName("Customer")
                                        .email("newuser@test.com")
                                        .password(encoder.encode("Password123!"))
                                        .role(UserRole.CUSTOMER)
                                        .build();

                        Account cutomer1Account1 = new Account(100.0, customer1);
                        Account cutomer1Account2 = new Account(50.0, customer1);

                        Account cutomer2Account = new Account(100.0, customer2);

                        customer1.addAccount(cutomer1Account1);
                        customer1.addAccount(cutomer1Account2);

                        customer2.addAccount(cutomer2Account);

                        customerRepo.save(customer1);
                        customerRepo.save(customer2);

                        Transaction tr1 = Transaction.builder()
                                        .fromAccount(cutomer1Account1)
                                        .toAccount(cutomer2Account)
                                        .amount(50.0)
                                        .type(TransactionType.TRANSFER)
                                        .build();

                        Transaction tr2 = Transaction.builder()
                                        .fromAccount(cutomer1Account1)
                                        .toAccount(cutomer2Account)
                                        .amount(150.0)
                                        .type(TransactionType.TRANSFER)
                                        .build();

                        Transaction tr3 = Transaction.builder()
                                        .fromAccount(cutomer2Account)
                                        .toAccount(cutomer1Account1)
                                        .amount(50.0)
                                        .type(TransactionType.TRANSFER)
                                        .build();

                        transactionRepo.saveAll(Arrays.asList(tr1, tr2, tr3));
                        System.out.println("=== DATABASE DATA SEEDED ===");
                };
        }

        @Bean
        @Profile("dev")
        public CommandLineRunner seedInitialDataInDatabase2(IBankWhitelistRepo repo) {
                return args -> {
                        BankWhitelist me = new BankWhitelist("SOLDEN", "13.60.62.171");
                        BankWhitelist Nazar = new BankWhitelist("NAZBON", null);
                        BankWhitelist YUSNIN = new BankWhitelist("YUSNIN", null);
                        BankWhitelist KULVLA = new BankWhitelist("KULVLA", null);
                        BankWhitelist GELDOV = new BankWhitelist("GELDOV", null);
                        BankWhitelist TSTBNK = new BankWhitelist("TSTBNK", null);
                        repo.saveAll(Arrays.asList(me, Nazar, YUSNIN, KULVLA, GELDOV));
                };

        }
}
