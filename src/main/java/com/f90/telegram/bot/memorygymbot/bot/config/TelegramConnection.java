package com.f90.telegram.bot.memorygymbot.bot.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramWebhookBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import org.telegram.telegrambots.updatesreceivers.DefaultWebhook;

import javax.annotation.PostConstruct;
import java.util.List;

@Component
@Slf4j
@Profile(value = {"!local"})
public class TelegramConnection {

    @Value("${spring.profiles.active}")
    private List<String> activeProfiles;

    private final TelegramWebhookBot memoryGymBot;

    TelegramConnection(TelegramWebhookBot telegramMemoryGymBot) {
        this.memoryGymBot = telegramMemoryGymBot;
    }

    @PostConstruct
    public void init() {
        try {
            log.info("init() - msg: starting TelegramConnection; profiles={}", activeProfiles);
            DefaultWebhook defaultWebhook = new DefaultWebhook();
            defaultWebhook.setInternalUrl("http://localhost:8090");
            defaultWebhook.registerWebhook(memoryGymBot);

            TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class, defaultWebhook);

            telegramBotsApi.registerBot(memoryGymBot, SetWebhook.builder().url("https://memorygymbot.oa.r.appspot.com/telegram").build());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
