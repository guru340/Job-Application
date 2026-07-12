package com.example.JobSerivce;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class JobSerivceApplication {

	public static void main(String[] args) {
		SpringApplication.run(JobSerivceApplication.class, args);
	}

}
