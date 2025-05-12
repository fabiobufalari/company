package com.bufalari.company;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles; // Importar ActiveProfiles

@SpringBootTest
@ActiveProfiles("test") // Ativa application-test.yml
class CompanyApplicationTests {

	@Test
	void contextLoads() {
		// Este teste verifica se o contexto da aplicação Spring carrega sem erros.
	}

}