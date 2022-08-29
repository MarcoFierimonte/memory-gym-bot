package com.f90.telegram.bot.memorygymbot.bot;

import com.f90.telegram.bot.memorygymbot.model.Word;
import com.f90.telegram.bot.memorygymbot.service.WordService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
                List<Word> result = new ArrayList<>();
                switch (command.getName()) {
                    case ADD:
                        wordService.add(command.getValue());
                        break;
                    case TEST:
                        result = wordService.test(command.getValue());
                        break;
                    case LEARN:
                        result = wordService.findAll();
                        break;
                    case DELETE:
                        wordService.delete(command.getValue());
                        break;
                    case UNKWOW:
                    default:
                        break;
                }
                if (result.isEmpty()) {
                    sendToChat(update, "Command=" + command.getName() + " processed.");
                } else {
                    String data = result.stream()
                            .map(Word::getIta)
                            .collect(Collectors.joining("\n"));
                    sendToChat(update, data);
                }
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
