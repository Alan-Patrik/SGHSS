package com.alanpatrik.sghss.api;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@OpenAPIDefinition
@SpringBootApplication
public class SghssApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(SghssApiApplication.class, args);
	}

}
