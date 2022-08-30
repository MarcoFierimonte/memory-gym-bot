package com.f90.telegram.bot.memorygymbot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class MemoryGymBotApplication {

	public static void main(String[] args) {
		SpringApplication.run(MemoryGymBotApplication.class, args);
	}

}
