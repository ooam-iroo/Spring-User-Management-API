package com.example.usermanagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class SpringUserManagementApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringUserManagementApiApplication.class, args);
	}

}
