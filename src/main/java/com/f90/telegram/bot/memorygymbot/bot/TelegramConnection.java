package com.f90.telegram.bot.memorygymbot.bot;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import javax.annotation.PostConstruct;

@Component
@Profile(value = "telegram")
public class TelegramConnection {

    private final MyMemoryGymBot memoryGymBot;

    TelegramConnection(MyMemoryGymBot memoryGymBot) {
        this.memoryGymBot = memoryGymBot;
    }

    @PostConstruct
    public void init() {
        try {
            TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
            // Register long polling bots. They work regardless type of TelegramBotsApi we are creating
            telegramBotsApi.registerBot(memoryGymBot);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
