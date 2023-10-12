package com.thehecklers.neoai;

import org.neo4j.cypherdsl.core.renderer.Configuration;
import org.neo4j.cypherdsl.core.renderer.Dialect;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class NeoaiApplication {
	@Bean
	public Configuration cypherDslConfiguration() {
		return Configuration.newConfig()
				.withDialect(Dialect.NEO4J_5)
				.build();
	}

	public static void main(String[] args) {
		SpringApplication.run(NeoaiApplication.class, args);
	}

}
