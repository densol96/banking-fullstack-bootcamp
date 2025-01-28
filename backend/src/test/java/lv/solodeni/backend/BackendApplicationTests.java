package lv.solodeni.backend;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(properties = {
		"DB_HOST=localhost",
		"DB_PORT=3306",
		"DB_USER=user",
		"DB_PASSWORD=password",
		"DB_NAME=mydb"
})
class BackendApplicationTests {

	@Test
	void contextLoads() {
	}

}
