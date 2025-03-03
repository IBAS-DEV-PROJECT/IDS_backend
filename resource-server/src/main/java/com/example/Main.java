package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.ApplicationPidFileWriter;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages = {"com.example", "com.spoti.api.auth"})
@EnableJpaRepositories(basePackages = {"com.spoti.api.auth.domain"}) // 모든 리포지토리 포함하도록 설정
@EntityScan(basePackages = {"com.spoti.api.auth.domain"}) // 모든 엔티티 포함하도록 설정
public class Main {
	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(Main.class);
		app.addListeners(new ApplicationPidFileWriter());
		app.run(args);
	}
}
