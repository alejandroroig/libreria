package com.ejemplo.libreria;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

// Con el perfil "dev" el test usa el PostgreSQL del Dev Container.
// Sin él, el contexto no encontraría ningún datasource y el test fallaría.
@SpringBootTest
@ActiveProfiles("dev")
class LibreriaApplicationTests {

	@Test
	void contextLoads() {
	}

}
