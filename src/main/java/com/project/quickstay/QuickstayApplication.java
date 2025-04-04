package com.project.quickstay;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class QuickstayApplication {

	public static void main(String[] args) {
		SpringApplication.run(QuickstayApplication.class, args);
	}

}
