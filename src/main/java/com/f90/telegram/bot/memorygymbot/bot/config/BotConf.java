package com.f90.telegram.bot.memorygymbot.bot.config;

import com.f90.telegram.bot.memorygymbot.bot.MemoryGymBotExecutor;
import com.f90.telegram.bot.memorygymbot.exception.InternalException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.bots.TelegramWebhookBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Configuration
@Slf4j
public class BotConf {

    @Profile(value = {"!local"})
    @Bean
    public TelegramWebhookBot memoryGymBotExecutor(MemoryGymBotExecutor memoryGymBotExecutor) {
        TelegramWebhookBot bot = new TelegramWebhookBot() {
            @Override
            public String getBotToken() {

                return memoryGymBotExecutor.getBotToken();
            }

            @Override
            public BotApiMethod<?> onWebhookUpdateReceived(Update update) {
                memoryGymBotExecutor.onUpdateReceived(update);
                return null;
            }

            @Override
            public String getBotPath() {
                return "webhook";
            }

            @Override
            public String getBotUsername() {
                return memoryGymBotExecutor.getBotUsername();
            }
        };
        memoryGymBotExecutor.setSendExecutor(msg -> {
            try {
                return bot.execute(msg);
            } catch (TelegramApiException e) {
                throw new InternalException("TelegramWebhookBot: Error during setting 'execute' implementation");
            }
        });
        log.info("Starting 'webhook' Telegram Bot.");
        return bot;
    }

    @Profile(value = {"local"})
    @Bean
    public TelegramLongPollingBot memoryGymBotExecutorLocal(MemoryGymBotExecutor memoryGymBotExecutor) {
        TelegramLongPollingBot bot = new TelegramLongPollingBot() {
            @Override
            public String getBotToken() {

                return memoryGymBotExecutor.getBotToken();
            }

            @Override
            public void onUpdateReceived(Update update) {
                memoryGymBotExecutor.onUpdateReceived(update);
            }

            @Override
            public String getBotUsername() {
                return memoryGymBotExecutor.getBotUsername();
            }
        };
        memoryGymBotExecutor.setSendExecutor(msg -> {
            try {
                return bot.execute(msg);
            } catch (TelegramApiException e) {
                throw new InternalException("TelegramLongPollingBot: Error during setting 'execute' implementation");
            }
        });
        log.info("Starting 'polling' Telegram Bot.");
        return bot;
    }
}
