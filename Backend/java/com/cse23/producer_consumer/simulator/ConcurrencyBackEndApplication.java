package com.cse23.producer_consumer.simulator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ConcurrencyBackEndApplication {

	public static final int PROCESSING_SECONDS = 5;
	public static final int RANDOM_PRODUCTS_SECONDS = 3;

	public static void main(String[] args) {
		SpringApplication.run(ConcurrencyBackEndApplication.class, args);
	}

}
