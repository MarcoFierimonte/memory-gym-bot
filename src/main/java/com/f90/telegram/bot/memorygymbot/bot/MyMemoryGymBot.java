package com.f90.telegram.bot.memorygymbot.bot;

import com.f90.telegram.bot.memorygymbot.dto.WordDTO;
import com.f90.telegram.bot.memorygymbot.model.User;
import com.f90.telegram.bot.memorygymbot.repo.UserRepo;
import com.f90.telegram.bot.memorygymbot.service.WordService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;

@Component
public class MyMemoryGymBot extends TelegramLongPollingBot {

    private static final Logger LOGGER = LoggerFactory.getLogger(MyMemoryGymBot.class);

    private final WordService wordService;
    private final UserRepo userRepo;

    public MyMemoryGymBot(WordService wordService, UserRepo userRepo) {
        this.wordService = wordService;
        this.userRepo = userRepo;
    }

    @Override
    public void onUpdateReceived(Update update) {
        try {
            LOGGER.info("onUpdateReceived() - IN");
            if (update.hasMessage()) {
                processMessage(update);
            } else if (update.hasCallbackQuery()) {
                processCallbackQuery(update);
            } else {
                LOGGER.info("onUpdateReceived() - msg: received not managed updates. Update=[{}]", update);
            }
        } catch (Exception e) {
            LOGGER.error("Error during 'onUpdateReceived'!", e);
            if (update.getMessage() != null) {
                Message message = update.getMessage();
                String username = message.getChat() != null ? message.getChat().getUserName() : null;
                Long chatId = message.getChat() != null ? update.getMessage().getChat().getId() : null;
                String text = message.getText() != null ? update.getMessage().getText() : null;
                LOGGER.error("Error during 'onUpdateReceived'; user={}, chatId={}, text={}", username, chatId, text);
            }
        }
        LOGGER.info("onUpdateReceived() - OUT");
    }

    private void processMessage(Update update) throws TelegramApiException {
        if (update.getMessage() != null) {
            Command command = Command.fromText(update.getMessage().getText());
            LOGGER.info("processMessage() - msg: command={}", command);
            if (command.getCmdType() == CustomCommand.CmdType.MENU) {
                processMenuCommand(update, command);
            } else {
                LOGGER.info("processMessage() - msg: UNKWOW command={}", command);
                sendToChat(update.getMessage(), "Please insert a valid command.", true);
            }
        } else {
            LOGGER.error("processMessage() - msg: received 'null' message");
        }
    }


    private void processMenuCommand(Update update, Command command) throws TelegramApiException {
        switch (command.getType()) {
            case START:
                User user = userRepo.findByChatId(update.getMessage().getChatId());
                if (user == null) {
                    userRepo.save(User.builder()
                            .chatId(update.getMessage().getChatId())
                            .lastTestPending(false)
                            .build());
                }
                sendKeyboard(update.getMessage(), "Press the button.", KeyboardBuilder.menuKeyboard());
                break;
            case TEST: {
                testUserMemory(update);
                break;
            }
            case LEARN: {
                List<WordDTO> words = wordService.test(5);
                sendToChat(update.getMessage(), EmojiUtil.NERD_FACE + " <b>LEARN THE WORDS</b> " + EmojiUtil.NERD_FACE, false);
                for (WordDTO current : words) {
                    sendToChat(update.getMessage(), MessageUtil.buildLearnWordText(current), false);
                }
                break;
            }
            case ADD:
            case DELETE:
            case UNKWOW:
            default:
                LOGGER.warn("processMenuCommand() - msg: unrecognized command={}", command);
                break;
        }
    }

    private void testUserMemory(Update update) throws TelegramApiException {
        List<WordDTO> words = wordService.test(3);
        sendToChat(update.getMessage(), EmojiUtil.STAR_FACE + " <b>GUESS THE WORDS</b> " + EmojiUtil.STAR_FACE, false);
        for (WordDTO current : words) {
            sendToChat(update.getMessage(), MessageUtil.buildGuessWordText(current), false);
        }
        sendKeyboard(update.getMessage(), "Press to next quiz!", KeyboardBuilder.doneKeyboard());
    }

    private void processCallbackQuery(Update update) {
        Message msg = update.getCallbackQuery().getMessage();
        if ("TEST_DONE".equals(update.getCallbackQuery().getData())) {
            User user = userRepo.findByChatId(msg.getChatId());
            user.setLastTestPending(false);
            userRepo.save(user);
        }
        LOGGER.warn("processCallbackQuery() - msg: received not managed 'callbackQuery' operation. Update=[{}]", update);
    }

    private void sendKeyboard(Message message, String text, ReplyKeyboard replyKeyboard) throws TelegramApiException {
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(message.getChatId());
        sendMessage.setReplyMarkup(replyKeyboard);
        sendMessage.setText(text);
        execute(sendMessage);
    }

    private void sendToChat(Message message, String text, boolean replyTo) throws TelegramApiException {
        SendMessage out = new SendMessage();
        out.setChatId(message.getChatId());
        if (replyTo) {
            out.setReplyToMessageId(message.getMessageId());
        }
        out.enableHtml(true);
        out.setText(text);
        execute(out);
    }

    @Scheduled(fixedDelay = 15000)
    public void sendToChatScheduled() throws TelegramApiException {
        List<User> users = userRepo.findByLastTestPendingIsFalse();
        for (User user : users) {
            LOGGER.info("sendToChatScheduled() - msg: send 'quiz' to user: {}", user.getChatId());
            Chat chat = new Chat();
            chat.setId(user.getChatId());
            Message msg = new Message();
            msg.setChat(chat);
            Update update = new Update();
            update.setMessage(msg);
            // send test
            testUserMemory(update);
            // update field
            user.setLastTestPending(true);
            userRepo.save(user);
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
