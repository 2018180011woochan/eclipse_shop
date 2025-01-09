package com.example.shop_project_v2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableJpaAuditing
@SpringBootApplication
@EnableScheduling
public class ShopProjectV2Application {

	public static void main(String[] args) {
		SpringApplication.run(ShopProjectV2Application.class, args);
	}

}
