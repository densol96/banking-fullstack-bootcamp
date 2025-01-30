package lv.solodeni.backend.config;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;

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
        public CommandLineRunner setUpWhitelist(IBankWhitelistRepo repo) {
                return args -> {
                        BankWhitelist me = new BankWhitelist("SOLDEN", "http://13.60.62.171");
                        BankWhitelist Nazar = new BankWhitelist("NAZBON", null);
                        BankWhitelist YUSNIN = new BankWhitelist("YUSNIN",
                                        "https://banking-application-53wg.onrender.com");
                        BankWhitelist KULVLA = new BankWhitelist("KULVLA", null);
                        BankWhitelist GELDOV = new BankWhitelist("GELDOV", null);
                        BankWhitelist TSTBNK = new BankWhitelist("TSTBNK", null);
                        BankWhitelist MIHAND = new BankWhitelist("MIHAND", null);
                        BankWhitelist DZHRUS = new BankWhitelist("DZHRUS", null);
                        BankWhitelist DENANT = new BankWhitelist("DENANT", null);
                        BankWhitelist PLEART = new BankWhitelist("PLEART", null);
                        BankWhitelist LIVANI = new BankWhitelist("LIVANI", null);
                        BankWhitelist ANTGUS = new BankWhitelist("ANTGUS", null);
                        BankWhitelist CELDOV = new BankWhitelist("CELDOV", null);
                        BankWhitelist SPOUGN = new BankWhitelist("SPOUGN", null);
                        BankWhitelist EITOVI = new BankWhitelist("EITOVI", null);
                        repo.saveAll(Arrays.asList(me, Nazar, YUSNIN, KULVLA, GELDOV, TSTBNK, MIHAND, DZHRUS, DENANT,
                                        PLEART, LIVANI, ANTGUS, CELDOV, SPOUGN, EITOVI));
                };
        }

        @Bean
        @Profile("dev_2")
        public CommandLineRunner test() {
                return args -> {
                        RestTemplate restTemplate = new RestTemplate();
                        String url = "https://banking-application-53wg.onrender.com/api/v1/accounts/transfer/external";

                        HttpHeaders headers = new HttpHeaders();
                        headers.setContentType(MediaType.APPLICATION_JSON);

                        Map<String, Object> requestBody = new HashMap<>();
                        requestBody.put("fromAccountNumber", "SOLDEN_905cab88c828");
                        requestBody.put("toAccountNumber", "YUSNIN_abcdef123456");
                        requestBody.put("amount", 132);

                        HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);
                        try {
                                ResponseEntity<JsonNode> response = restTemplate.postForEntity(url, request,
                                                JsonNode.class);
                                // Needs additional logic here but i njeed to check the response structure first
                                System.out.println(response);
                                System.out.println("RESPONSE FROM THE EXTERNAL SYSTEM: "
                                                + response.getBody().get("message").asText());
                                // return TransferSuccessMessage
                        } catch (HttpClientErrorException | HttpServerErrorException e) {
                                System.out.println("transfer-external === Http* Error => " + e.getMessage());
                                // throw new FailedTransactionException();
                        } catch (Exception e) {
                                System.out.println(e.getMessage());
                                System.out.println("transfer-external === UNEXPECTED ERROR");
                                // throw new FailedTransactionException();
                        }
                };
        }
}
