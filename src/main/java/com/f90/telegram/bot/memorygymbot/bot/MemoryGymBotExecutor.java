package com.f90.telegram.bot.memorygymbot.bot;

import com.f90.telegram.bot.memorygymbot.dto.WordDTO;
import com.f90.telegram.bot.memorygymbot.model.User;
import com.f90.telegram.bot.memorygymbot.service.UserService;
import com.f90.telegram.bot.memorygymbot.service.WordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;

import java.util.List;
import java.util.Optional;

@Component
@Slf4j
public class MemoryGymBotExecutor {

    private final WordService wordService;
    private final UserService userService;
    private Send sendExecutor;

    @Value("${telegram.token}")
    private String token;

    protected MemoryGymBotExecutor(WordService wordService, UserService userService) {
        this.wordService = wordService;
        this.userService = userService;
    }

    public void setSendExecutor(Send sendExecutor) {
        this.sendExecutor = sendExecutor;
    }

    public void onUpdateReceived(Update update) {
        log.info("onUpdateReceived() - IN");
        if (update != null) {
            try {
                if (update.hasMessage()) {
                    processMessage(update);
                } else if (update.hasCallbackQuery()) {
                    processCallbackQuery(update);
                } else {
                    log.info("onUpdateReceived() - msg: received not managed updates. Update=[{}]", update);
                    sendToChat(update.getMessage(), "Please insert a valid command.", true);
                }
            } catch (Exception e) {
                log.error("Error during 'onUpdateReceived'!", e);
                logError(update.getMessage());
            }
        }
        log.info("onUpdateReceived() - OUT");
    }

    private void logError(Message message) {
        if (message != null) {
            String username = message.getChat() != null ? message.getChat().getUserName() : null;
            Long chatId = message.getChat() != null ? message.getChat().getId() : null;
            String text = message.getText() != null ? message.getText() : null;
            log.error("Error during 'onUpdateReceived'; user={}, chatId={}, text={}", username, chatId, text);
        }
    }

    private void processMessage(Update update) {
        Command command = Command.fromText(update.getMessage().getText());
        log.info("processMessage() - msg: command={}", command);
        if (command.getCmdType() == CustomCommand.CmdType.MENU) {
            switch (command.getType()) {
                case START: {
                    User user = userService.findByChatId(update.getMessage().getChatId());
                    if (user == null) {
                        userService.save(User.builder()
                                .chatId(update.getMessage().getChatId())
                                .userId(update.getMessage().getFrom().getId())
                                .userName(userService.getUserName(update))
                                .testNotificationEnabled(false)
                                .build());
                        wordService.init(update.getMessage().getChatId());
                        log.info("processMenuCommand() - msg: user init completed. User={}", update.getMessage().getFrom().getId());
                    }
                    sendKeyboard(update.getMessage(), "Press the button.", KeyboardBuilder.menuKeyboard(update.getMessage().getChatId()));
                    break;
                }
                case TEST: {
                    testUserMemory(update);
                    break;
                }
                case LEARN: {
                    List<WordDTO> words = wordService.test(update.getMessage().getChatId(), 5);
                    if (!words.isEmpty()) {
                        sendToChat(update.getMessage(), "➖➖➖➖➖➖➖➖➖➖", false);
                        sendToChat(update.getMessage(), EmojiUtil.NERD_FACE + " <b>LEARN THE WORDS</b> " + EmojiUtil.NERD_FACE, false);
                        for (WordDTO current : words) {
                            sendToChat(update.getMessage(), MessageUtil.buildLearnWordText(current), false);
                        }
                    } else {
                        sendToChat(update.getMessage(), "No words in your dictionary! Add new ones", false);
                    }
                    break;
                }
                default:
                    log.warn("processMenuCommand() - msg: not managed command={}", command);
                    break;
            }
        } else {
            log.info("processMessage() - msg: UNKWOW command={}", command);
            sendToChat(update.getMessage(), "Please insert a valid command.", true);
        }
    }

    private void testUserMemory(Update update) {
        List<WordDTO> words = wordService.test(update.getMessage().getChatId(), 4);
        if (!words.isEmpty()) {
            log.info("sendToChatScheduled() - msg: send 'quiz' to user: {}", update.getMessage().getChatId());
            sendToChat(update.getMessage(), "➖➖➖➖➖➖➖➖➖➖", false);
            sendToChat(update.getMessage(), EmojiUtil.STAR_FACE + " <b>GUESS THE WORDS</b> " + EmojiUtil.STAR_FACE, false);
            for (WordDTO current : words) {
                sendToChat(update.getMessage(), MessageUtil.buildGuessWordText(current), false);
            }
        } else {
            sendToChat(update.getMessage(), "No words in your dictionary! Add new ones", false);
        }
    }

    private void processCallbackQuery(Update update) {
        Message msg = update.getCallbackQuery().getMessage();
        if ("TEST_DONE".equals(update.getCallbackQuery().getData())) {
            User user = userService.findByChatId(msg.getChatId());
            user.setTestNotificationEnabled(false);
            userService.save(user);
            sendToChat(update.getCallbackQuery().getMessage(), "Current 'test' completed! " + EmojiUtil.HAPPY_FACE, true);
        }
        log.warn("processCallbackQuery() - msg: received not managed 'callbackQuery' operation. Update=[{}]", update);
    }

    private void sendKeyboard(Message message, String text, ReplyKeyboard replyKeyboard) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(message.getChatId());
        sendMessage.setReplyMarkup(replyKeyboard);
        sendMessage.setText(text);
        sendExecutor.execute(sendMessage);
    }

    private void sendToChat(Message message, String text, boolean replyTo) {
        SendMessage out = new SendMessage();
        out.setChatId(message.getChatId());
        if (replyTo) {
            out.setReplyToMessageId(message.getMessageId());
        }
        out.enableHtml(true);
        out.setText(text);
        sendExecutor.execute(out);
    }

    public void sendTestToAllUsers() {
        List<User> users = userService.findAll(Optional.of(User.builder().testNotificationEnabled(true).build()));
        for (User user : users) {
            Chat chat = new Chat();
            chat.setId(user.getChatId());
            Message msg = new Message();
            msg.setChat(chat);
            Update update = new Update();
            update.setMessage(msg);
            log.info("sendTestToAllUsers() - msg: sending 'test' to user={}", user.getUserName());
            // send test
            testUserMemory(update);
            // update field
            user.setTestNotificationEnabled(true);
            userService.save(user);
        }
    }

    public void addWordsToAllUsers() {
        List<User> users = userService.findAll(Optional.empty());
        for (User user : users) {
            Chat chat = new Chat();
            chat.setId(user.getChatId());
            Message msg = new Message();
            msg.setChat(chat);
            Update update = new Update();
            update.setMessage(msg);
            wordService.init(user.getChatId());
            log.info("addWordsToAllUsers() - msg: completed for user={}", user.getChatId());
        }
    }

    public String getBotToken() {
        return token;
    }


    public String getBotUsername() {
        return "MemoryGymBot";
    }


}
