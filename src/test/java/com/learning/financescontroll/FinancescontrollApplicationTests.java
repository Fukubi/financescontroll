package com.learning.financescontroll;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootTest
class FinancescontrollApplicationTests {
	
	@Autowired
	private PasswordEncoder pass;

	@Test
	void contextLoads() {
		
		System.out.println(pass.encode("senha123"));
	}

}
