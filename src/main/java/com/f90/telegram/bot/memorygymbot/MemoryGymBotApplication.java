package com.f90.telegram.bot.memorygymbot;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(scanBasePackages = "com.f90")
@EnableScheduling
@EnableAspectJAutoProxy
@Slf4j
public class MemoryGymBotApplication {

	public static void main(String[] args) {
		log.info("Starting 'MemoryGymBotApplication' Telegram Bot.");
		SpringApplication.run(MemoryGymBotApplication.class, args);
	}

}
