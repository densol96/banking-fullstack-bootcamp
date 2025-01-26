package lv.solodeni.backend.config;

import java.util.Arrays;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;

import lv.solodeni.backend.model.Account;
import lv.solodeni.backend.model.Customer;
import lv.solodeni.backend.model.Transaction;
import lv.solodeni.backend.model.enums.Status;
import lv.solodeni.backend.model.enums.TransactionType;
import lv.solodeni.backend.model.enums.UserRole;
import lv.solodeni.backend.repository.ICustomerRepo;
import lv.solodeni.backend.repository.ITransactionRepo;

@Configuration
public class DataInitializerConfig {
        @Bean
        @Profile("set_up")
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

                        Transaction tr1 = new Transaction(cutomer1Account1, cutomer2Account, 50.0,
                                        Status.SUCCESS, TransactionType.TRANSFER, null);
                        Transaction tr2 = new Transaction(cutomer1Account1, cutomer2Account, 150.0,
                                        Status.SUCCESS, TransactionType.TRANSFER, null);
                        Transaction tr3 = new Transaction(cutomer2Account, cutomer1Account1, 50.0,
                                        Status.SUCCESS, TransactionType.TRANSFER, null);

                        transactionRepo.saveAll(Arrays.asList(tr1, tr2, tr3));
                        System.out.println("======> " + customer1.getId().toString());
                };
        }
}
