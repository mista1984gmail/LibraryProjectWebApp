package com.example.LibraryProjectWebApp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class LibraryProjectWebAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(LibraryProjectWebAppApplication.class, args);
	}

}
