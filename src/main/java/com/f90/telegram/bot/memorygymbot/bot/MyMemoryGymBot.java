package com.f90.telegram.bot.memorygymbot.bot;

import com.f90.telegram.bot.memorygymbot.service.WordService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class MyMemoryGymBot extends TelegramLongPollingBot {

    private static final Logger LOGGER = LoggerFactory.getLogger(MyMemoryGymBot.class);

    private final WordService wordService;

    public MyMemoryGymBot(WordService wordService) {
        this.wordService = wordService;
    }

    @Override
    public void onUpdateReceived(Update update) {
        // Checking if the update has message and text
        if (update.hasMessage() && update.getMessage().hasText()) {

            if (update.getMessage().isCommand()) {
                Command command = Command.fromText(update.getMessage().getText());
                switch (command.getName()) {
                    case ADD:
                        wordService.add(command.getValue());
                        break;
                    case LEARN:
                        break;
                    case DELETE:
                        wordService.delete(command.getValue());
                        break;
                    case UNKWOW:
                    default:
                        break;
                }

                sendToChat(update, "Send command=" + command);
            } else {
                sendToChat(update, "Please insert a valid command.");
            }
        }
    }


    private void sendToChat(Update update, String text) {
        // Creating object of SendMessage
        SendMessage message = new SendMessage();
        // Setting chat id
        message.setChatId(update.getMessage().getChatId().toString());
        // Setting reply to message id
        message.setReplyToMessageId(update.getMessage().getMessageId());
        // Getting and setting received message text
        message.setText(text);
        try {
            // Sending message
            execute(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getBotToken() {
        return "5430654397:AAFFfiCrefgArpKkBVBSaAUxu60TgPcGjjs";
    }


    @Override
    public String getBotUsername() {
        return "MemoryGymBot";
    }

}
