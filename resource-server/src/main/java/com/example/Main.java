package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.ApplicationPidFileWriter;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages = {"com.example", "com.spoti.api.auth", "com.example.web"})
@EnableJpaRepositories(basePackages = {
	"com.spoti.api.auth.domain",
	"com.example.web.recommending.repository" //정확하게 패키지 명시
})
@EntityScan(basePackages = {
	"com.spoti.api.auth.domain",
	"com.example.web.recommending.domain"
})
public class Main {
	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(Main.class);
		app.addListeners(new ApplicationPidFileWriter());
		app.run(args);
	}
}
