package com.f90.telegram.bot.memorygymbot.bot.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import javax.annotation.PostConstruct;

@Component
@Slf4j
@Profile(value = {"local"})
public class TelegramConnectionLocal {

    private final TelegramLongPollingBot memoryGymBot;

    TelegramConnectionLocal(TelegramLongPollingBot memoryGymBotExecutorLocal) {
        this.memoryGymBot = memoryGymBotExecutorLocal;
    }

    @PostConstruct
    public void init() {
        try {
            log.info("init() - msg: starting TelegramConnection 'local'");
            TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
            // Register long polling bots. They work regardless type of TelegramBotsApi we are creating
            telegramBotsApi.registerBot(memoryGymBot);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
