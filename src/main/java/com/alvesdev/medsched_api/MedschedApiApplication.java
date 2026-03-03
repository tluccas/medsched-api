package com.alvesdev.medsched_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class MedschedApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(MedschedApiApplication.class, args);
	}

}
