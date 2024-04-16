package com.kelompok1.kloun;

import com.kelompok1.kloun.config.CorsConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import(CorsConfig.class)
public class KlounApplication {

	public static void main(String[] args) {
		SpringApplication.run(KlounApplication.class, args);
	}

}
