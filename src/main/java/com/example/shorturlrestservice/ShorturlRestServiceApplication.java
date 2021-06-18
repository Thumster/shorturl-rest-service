package com.example.shorturlrestservice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Slf4j
public class ShorturlRestServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ShorturlRestServiceApplication.class, args);
		log.info("Application started successfully!");
	}

}
