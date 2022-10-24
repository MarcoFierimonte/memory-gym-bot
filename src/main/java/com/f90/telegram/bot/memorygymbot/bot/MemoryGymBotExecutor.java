package com.f90.telegram.bot.memorygymbot.bot;

import com.f90.telegram.bot.memorygymbot.dto.IrregularVerbDTO;
import com.f90.telegram.bot.memorygymbot.dto.UserDTO;
import com.f90.telegram.bot.memorygymbot.dto.WordDTO;
import com.f90.telegram.bot.memorygymbot.mapper.UserMapper;
import com.f90.telegram.bot.memorygymbot.model.User;
import com.f90.telegram.bot.memorygymbot.service.IrrebularVerbService;
import com.f90.telegram.bot.memorygymbot.service.UserService;
import com.f90.telegram.bot.memorygymbot.service.WordService;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class MemoryGymBotExecutor {

    private final WordService wordService;
    private final UserService userService;
    private final IrrebularVerbService irrebularVerbService;
    private Send sendExecutor;

    @Value("${telegram.token}")
    private String token;

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
                case TEST: {
                    testAction(update);
                    break;
                }
                case LEARN: {
                    learnAction(update);
                    break;
                }
                case VERBS: {
                    verbsAction(update);
                    break;
                }
                default:
                case START: {
                    startAction(update);
                    break;
                }
            }
        } else {
            log.info("processMessage() - msg: UNKWOW command={}", command);
            sendToChat(update.getMessage(), "Please insert a valid command.", true);
            sendKeyboard(update.getMessage(), "Press the button.", KeyboardBuilder.menuKeyboard(update.getMessage().getChatId()));

        }
    }

    private void testAction(Update update) {
        List<WordDTO> words = wordService.test(update.getMessage().getChatId(), 4);
        if (!words.isEmpty()) {
            log.info("sendToChatScheduled() - msg: send 'quiz' to user: {}", update.getMessage().getChatId());
            sendToChat(update.getMessage(), EmojiUtil.LINE, false);
            sendToChat(update.getMessage(), EmojiUtil.STAR_FACE + " <b>GUESS THE WORDS</b> " + EmojiUtil.STAR_FACE, false);
            for (WordDTO current : words) {
                sendToChat(update.getMessage(), MessageUtil.buildGuessWordText(current), false);
            }
        } else {
            sendToChat(update.getMessage(), "No words in your dictionary! Add new ones", false);
        }
    }

    private void learnAction(Update update) {
        List<WordDTO> words = wordService.test(update.getMessage().getChatId(), 4);
        if (!words.isEmpty()) {
            sendToChat(update.getMessage(), EmojiUtil.LINE, false);
            sendToChat(update.getMessage(), EmojiUtil.NERD_FACE + " <b>LEARN THE WORDS</b> " + EmojiUtil.NERD_FACE, false);
            for (WordDTO current : words) {
                sendToChat(update.getMessage(), MessageUtil.buildLearnWordText(current), false);
            }
        } else {
            sendToChat(update.getMessage(), "No words in your dictionary! Add new ones", false);
        }
    }

    private void verbsAction(Update update) {
        List<IrregularVerbDTO> verbs = irrebularVerbService.random(4);
        if (!verbs.isEmpty()) {
            sendToChat(update.getMessage(), EmojiUtil.LINE, false);
            sendToChat(update.getMessage(), EmojiUtil.NERD_FACE + " <b>LEARN THE VERBS</b> " + EmojiUtil.NERD_FACE, false);
            for (IrregularVerbDTO current : verbs) {
                sendToChat(update.getMessage(), MessageUtil.buildGuessVerbText(current), false);
            }
        } else {
            sendToChat(update.getMessage(), "No verbs in your dictionary! Add new ones", false);
        }
    }

    private void startAction(Update update) {
        UserDTO user = userService.findByChatId(update.getMessage().getChatId());
        if (user == null) {
            userService.save(User.builder()
                    .chatId(update.getMessage().getChatId())
                    .userId(update.getMessage().getFrom().getId())
                    .userName(userService.getUserName(update))
                    .testNotificationEnabled(true)
                    .build());
            wordService.init(update.getMessage().getChatId());
            log.info("processMenuCommand() - msg: user init completed. User={}", update.getMessage().getFrom().getId());
        }
        sendKeyboard(update.getMessage(), "Press the button.", KeyboardBuilder.menuKeyboard(update.getMessage().getChatId()));
    }

    private void processCallbackQuery(Update update) {
        Message msg = update.getCallbackQuery().getMessage();
        if ("TEST_DONE".equals(update.getCallbackQuery().getData())) {
            UserDTO user = userService.findByChatId(msg.getChatId());
            user.setTestNotificationEnabled(false);
            userService.save(UserMapper.toUser(user));
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
        List<UserDTO> users = userService.findAll(Optional.of(User.builder().testNotificationEnabled(true).build()));
        for (UserDTO user : users) {
            Chat chat = new Chat();
            chat.setId(user.getChatId());
            Message msg = new Message();
            msg.setChat(chat);
            Update update = new Update();
            update.setMessage(msg);
            log.info("sendTestToAllUsers() - msg: sending 'test' to user={}", user.getUserName());
            // send test
            testAction(update);
            // update field
            user.setTestNotificationEnabled(true);
            userService.save(UserMapper.toUser(user));

        }
    }

    public void addWordsToAllUsers() {
        List<UserDTO> users = userService.findAll(Optional.empty());
        for (UserDTO user : users) {
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

    public void newBotUpgrades() {
        List<UserDTO> users = userService.findAll(Optional.empty());
        for (UserDTO user : users) {
            Chat chat = new Chat();
            chat.setId(user.getChatId());
            Message msg = new Message();
            msg.setChat(chat);
            Update update = new Update();
            update.setMessage(msg);
            sendToChat(update.getMessage(), EmojiUtil.LINE, false);
            sendToChat(update.getMessage(), EmojiUtil.PARTY + EmojiUtil.PARTY + EmojiUtil.PARTY + " <b>NEW BOT UPGRADES</b> " + EmojiUtil.PARTY + EmojiUtil.PARTY + EmojiUtil.PARTY, false);
            sendToChat(update.getMessage(), EmojiUtil.MEMO + " New VERBS button: learn irregular verbs.", false);
            log.info("newBotUpgrade() - msg: completed for user={}", user.getChatId());
        }
    }

    public String getBotToken() {
        return token;
    }


    public String getBotUsername() {
        return "MemoryGymBot";
    }


}
