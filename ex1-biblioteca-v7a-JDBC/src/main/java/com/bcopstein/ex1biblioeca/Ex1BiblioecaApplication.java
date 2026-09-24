package com.bcopstein.ex1biblioeca;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = { "com.bcopstein" })
public class Ex1BiblioecaApplication {
	public static void main(String[] args) {
		SpringApplication.run(Ex1BiblioecaApplication.class, args);
	}

}

