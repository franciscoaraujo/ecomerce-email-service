package br.com.fiaptcc.msemailservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class MsEmailServiceApplication {
	public static void main(String[] args) {
		SpringApplication.run(MsEmailServiceApplication.class, args);
	}

}
