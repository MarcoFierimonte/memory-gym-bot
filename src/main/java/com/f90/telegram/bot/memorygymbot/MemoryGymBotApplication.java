package com.f90.telegram.bot.memorygymbot;

import com.f90.telegram.bot.memorygymbot.bot.MyMemoryGymBot;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@SpringBootApplication
public class MemoryGymBotApplication {

	public static void main(String[] args) {
		SpringApplication.run(MemoryGymBotApplication.class, args);

		try {
			TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
			// Register long polling bots. They work regardless type of TelegramBotsApi we are creating
			telegramBotsApi.registerBot(new MyMemoryGymBot());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
