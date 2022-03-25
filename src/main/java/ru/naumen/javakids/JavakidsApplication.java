package ru.naumen.javakids;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories("ru.naumen.javakids.repository")
@EntityScan("ru/naumen/javakids//model")
public class JavakidsApplication {

	public static void main(String[] args) {
		SpringApplication.run(JavakidsApplication.class, args);
	}

}
