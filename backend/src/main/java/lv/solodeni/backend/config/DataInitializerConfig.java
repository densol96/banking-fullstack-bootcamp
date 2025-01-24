package lv.solodeni.backend.config;

import java.util.Arrays;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lv.solodeni.backend.model.Account;
import lv.solodeni.backend.model.Customer;
import lv.solodeni.backend.model.Transaction;
import lv.solodeni.backend.model.enums.Status;
import lv.solodeni.backend.repository.ICustomerRepo;
import lv.solodeni.backend.repository.ITransactionRepo;

@Configuration
public class DataInitializerConfig {
    @Bean
    public CommandLineRunner seedInitialDataInDatabase(ICustomerRepo customerRepo, ITransactionRepo transactionRepo) {
        return args -> {
            Customer customer1 = new Customer("Deniss", "Solovjovs", "solodeni@test.com", "password123");
            Customer customer2 = new Customer("New", "Customer", "newuser@test.com", "password123");

            Account cutomer1Account = new Account(100.0, customer1);
            Account cutomer2Account = new Account(100.0, customer2);

            customer1.addAccount(cutomer1Account);
            customer2.addAccount(cutomer2Account);

            customerRepo.save(customer1);
            customerRepo.save(customer2);

            Transaction tr1 = new Transaction(cutomer1Account, cutomer2Account, 50.0,
                    Status.SUCCESS);
            Transaction tr2 = new Transaction(cutomer1Account, cutomer2Account, 150.0,
                    Status.SUCCESS);
            Transaction tr3 = new Transaction(cutomer2Account, cutomer1Account, 50.0,
                    Status.SUCCESS);

            transactionRepo.saveAll(Arrays.asList(tr1, tr2, tr3));
            System.out.println("======> " + customer1.getId().toString());
        };
    }
}
