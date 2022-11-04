package com.f90.telegram.bot.memorygymbot.bot;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

public interface Send {

    Message execute(SendMessage sendDocument);
}
