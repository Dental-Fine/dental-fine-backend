package com.DentalFine.Dental_Fine_BackEnd;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class DentalFineBackEndApplication {

	public static void main(String[] args) {
		SpringApplication.run(DentalFineBackEndApplication.class, args);
	}

}
