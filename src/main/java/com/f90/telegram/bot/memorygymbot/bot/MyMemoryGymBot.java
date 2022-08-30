package com.f90.telegram.bot.memorygymbot.bot;

import com.f90.telegram.bot.memorygymbot.bot.keyboard.KeyboardBuilder;
import com.f90.telegram.bot.memorygymbot.model.Word;
import com.f90.telegram.bot.memorygymbot.service.WordService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.ActionType;
import org.telegram.telegrambots.meta.api.methods.send.SendChatAction;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class MyMemoryGymBot extends TelegramLongPollingBot {

    private static final Logger LOGGER = LoggerFactory.getLogger(MyMemoryGymBot.class);
    private static final Long CHAT_ID = 69501949L;

    private final WordService wordService;

    public MyMemoryGymBot(WordService wordService) {
        this.wordService = wordService;
    }

    /* TODO:
    1. create a menù with buttons: TEST, LEARN, ADD, DELETE
    2. if button TEST is pressed reply with 3 words to play with
    3. if button LEARN is pressed reply with 10 words completed of ita,eng and pronunciation set
    4. if button ADD or DELETE is pressed reply with the pre-built commands /add <eng>;<ita> or /delete <ita>
     */

    @Override
    public void onUpdateReceived(Update update) {
        LOGGER.info("onUpdateReceived() - IN");
        // Checking if the update has message and text
        if (update.hasMessage() && update.getMessage().hasText()) {

            if (update.getMessage().isCommand()) {
                Command command = Command.fromText(update.getMessage().getText());
                LOGGER.info("onUpdateReceived() - msg: command={}", command);

                boolean showMenu = false;
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
                    case PLAY:
                        sendKeyboard(update, KeyboardBuilder.getMainMenuKeyboard());

                        sendKeyboard(update, KeyboardBuilder.keyboard2());
                        showMenu = true;
                        break;
                    case UNKWOW:
                    default:

                        break;
                }

                if(!showMenu) {
                    if (result.isEmpty()) {
                        sendToChat(update, "Command=" + command.getName() + " processed.");
                    } else {
                        String data = result.stream()
                                .map(Word::getIta)
                                .collect(Collectors.joining("\n"));
                        sendToChat(update, data);
                    }
                }
            } else {
                sendToChat(update, "Please insert a valid command.");
            }
        }
        else if (update.hasCallbackQuery()) {
            CallbackQuery callbackQuery = update.getCallbackQuery();
            String data = callbackQuery.getData();
            String chatId = callbackQuery.getMessage().getChat().getId().toString();
            SendChatAction sendChatAction = new SendChatAction();
            if (data.equals("callback text1")) {
                sendChatAction.setChatId(chatId);
                SendMessage sendMessage = new SendMessage();
                sendMessage.setText("callback from text1");
                sendMessage.setChatId(chatId);
                try {
                    sendChatAction.setAction(ActionType.TYPING);
                    execute(sendChatAction);
                    execute(sendMessage);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }
        }
        LOGGER.info("onUpdateReceived() - OUT");
    }

    private void sendKeyboard(Update update, ReplyKeyboard replyKeyboard) {
        try {
            SendMessage sendMessage = new SendMessage();
            sendMessage.enableMarkdown(true);
            sendMessage.setChatId(update.getMessage().getChatId());
            sendMessage.setReplyToMessageId(update.getMessage().getMessageId());
            sendMessage.setReplyMarkup(replyKeyboard);
            sendMessage.setText("prova");

            execute(sendMessage);
        } catch (Exception e) {
            String username = update.getMessage().getChat().getUserName();
            Long chatId = update.getMessage().getChat().getId();
            LOGGER.error("Error during 'sendToChat'; user={}, chatId={}", username, chatId, e);
        }
    }

    private void sendToChat(Update update, String text) {
        try {
            // Creating object of SendMessage
            SendMessage message = new SendMessage();
            // Setting chat id
            message.setChatId(update.getMessage().getChatId().toString());
            // Setting reply to message id
            message.setReplyToMessageId(update.getMessage().getMessageId());
            // Getting and setting received message text
            message.setText(text);

            // Sending message
            execute(message);
        } catch (Exception e) {
            String username = update.getMessage().getChat().getUserName();
            Long chatId = update.getMessage().getChat().getId();
            LOGGER.error("Error during 'sendToChat'; user={}, chatId={}", username, chatId, e);
        }
    }

    private void sendToChat(Long chatId, String text) {
        try {
            // Creating object of SendMessage
            SendMessage message = new SendMessage();
            // Setting chat id
            message.setChatId(chatId);
            // Getting and setting received message text
            message.setText(text);
            // Sending message
            execute(message);
        } catch (Exception e) {
            LOGGER.error("Error during 'sendToChat'; chatId={}", chatId, e);
        }
    }

    //@Scheduled(fixedDelay = 15000)
    public void sendToChatScheduled() {
        LOGGER.info("sendToChatScheduled() - msg: started job");
        sendToChat(CHAT_ID, "test");
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
