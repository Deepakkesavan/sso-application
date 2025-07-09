package com.clarium;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties
public class SsoBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(SsoBackendApplication.class, args);
	}

}
