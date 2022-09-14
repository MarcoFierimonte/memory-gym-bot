package com.f90.telegram.bot.memorygymbot.bot;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import org.telegram.telegrambots.updatesreceivers.DefaultWebhook;

import javax.annotation.PostConstruct;
import java.util.List;

@Component
@Slf4j
public class TelegramConnection {

    @Value("${spring.profiles.active}")
    private List<String> activeProfiles;

    private final MyMemoryGymBot memoryGymBot;

    TelegramConnection(MyMemoryGymBot memoryGymBot) {
        this.memoryGymBot = memoryGymBot;
    }

    @PostConstruct
    public void init() {
        try {
            log.info("init() - msg: starting TelegramConnection; profiles={}", activeProfiles);
            DefaultWebhook defaultWebhook = new DefaultWebhook();
            if(activeProfiles.contains("local")) {
                defaultWebhook.setInternalUrl("http://localhost:8082");
            }
            defaultWebhook.registerWebhook(memoryGymBot);

            TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class, defaultWebhook);
            telegramBotsApi.registerBot(memoryGymBot, SetWebhook.builder().url("https://memorygymbot.oa.r.appspot.com").build());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
