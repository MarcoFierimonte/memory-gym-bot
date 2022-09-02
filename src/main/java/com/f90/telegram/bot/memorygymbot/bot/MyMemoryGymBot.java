package com.f90.telegram.bot.memorygymbot.bot;

import com.f90.telegram.bot.memorygymbot.exception.InternalException;
import com.f90.telegram.bot.memorygymbot.model.Word;
import com.f90.telegram.bot.memorygymbot.service.WordService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.ActionType;
import org.telegram.telegrambots.meta.api.methods.send.SendChatAction;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;

@Component
public class MyMemoryGymBot extends TelegramLongPollingBot {

    private static final Logger LOGGER = LoggerFactory.getLogger(MyMemoryGymBot.class);
    private static final Long CHAT_ID = 69501949L;

    private final WordService wordService;

    public MyMemoryGymBot(WordService wordService) {
        this.wordService = wordService;
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
            } else if (command.getCmdType() == CustomCommand.CmdType.ACTION) {
                processActionCommand(update, command);
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
                sendKeyboard(update.getMessage(), "Press the button.", KeyboardBuilder.menuKeyboard());
                break;
            case TEST: {
                List<Word> words = wordService.test(3);
                sendToChat(update.getMessage(), EmojiUtil.STAR_FACE + " <b>GUESS THE WORDS</b> " + EmojiUtil.STAR_FACE, false);
                for (Word current : words) {
                    sendToChat(update.getMessage(), MessageUtil.buildGuessWordText(current), false);
                }
                break;
            }
            case LEARN: {
                List<Word> words = wordService.test(5);
                sendToChat(update.getMessage(), EmojiUtil.NERD_FACE + " <b>LEARN THE WORDS</b> " + EmojiUtil.NERD_FACE, false);
                for (Word current : words) {
                    sendToChat(update.getMessage(), MessageUtil.buildLearnWordText(current), false);
                }
                break;
            }
            case ADD:
                sendToChat(update.getMessage(), "Replace [ita;eng] with the word you want add", false);
                sendToChat(update.getMessage(), "/add [ita;eng]", false);
                break;
            case DELETE:
                sendToChat(update.getMessage(), "Replace 'ita' with the word you want delete", false);
                sendToChat(update.getMessage(), "/delete [ita]", false);
                break;
            case UNKWOW:
            default:
                break;
        }
    }

    private void processActionCommand(Update update, Command command) throws TelegramApiException {
        switch (command.getType()) {
            case ADD_WORD: {
                String inputWord = command.getValue().trim();
                if (!(inputWord.startsWith("[") && inputWord.endsWith("]"))) {
                    throw new InternalException("Error during ADD WORD operation. Invalid format: " + command.getValue() + "; correct format: /add [ita;eng]");
                }
                inputWord = inputWord.substring(1, inputWord.length() - 1);
                String[] values = inputWord.split(";");
                if (values.length != 2) {
                    throw new InternalException("Error during ADD WORD operation. Invalid format: " + command.getValue() + "; correct format: /add [ita;eng]");
                }
                String wordToAddIta = values[0].trim();
                String wordToAddEng = values[1].trim();
                wordService.add(Word.builder().ita(wordToAddIta).eng(wordToAddEng).build());
                sendToChat(update.getMessage(), "Word added!", false);
                break;
            }
            case DELETE_WORD: {
                String inputWord = command.getValue().trim();
                if (!(inputWord.startsWith("[") && inputWord.endsWith("]"))) {
                    throw new InternalException("Error during DELETE WORD operation. Invalid format: " + command.getValue() + "; correct format: /delete [ita]");
                }
                inputWord = inputWord.substring(1, inputWord.length() - 1);
                if (StringUtils.isNotEmpty(inputWord)) {
                    Word toDelete = wordService.findByIta(inputWord);
                    if (toDelete == null) {
                        throw new InternalException("Error during DELETE WORD operation; word not found: " + inputWord);
                    }
                    wordService.delete(toDelete.getIta());
                    sendToChat(update.getMessage(), "Word deleted!", false);
                } else {
                    throw new InternalException("Error during DELETE WORD operation; 'wordItaToDelete' invalid format:" + inputWord);
                }
                break;
            }
            case UNKWOW:
            default:
                break;
        }
    }

    private void processCallbackQuery(Update update) {
        LOGGER.warn("processCallbackQuery() - msg: received not managed 'callbackQuery' operation. Update=[{}]", update);
    }

    private void sendKeyboard(Message message, String text, ReplyKeyboard replyKeyboard) throws TelegramApiException {
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(message.getChatId());
        sendMessage.setReplyToMessageId(message.getMessageId());
        sendMessage.setReplyMarkup(replyKeyboard);
        sendMessage.setText(text);
        SendChatAction sendChatAction = new SendChatAction();
        sendChatAction.setChatId(message.getChatId());
        sendChatAction.setAction(ActionType.TYPING);
        execute(sendChatAction);
        execute(sendMessage);
    }

    private Message sendToChat(Message message, String text, boolean replyTo) throws TelegramApiException {
        SendMessage out = new SendMessage();
        out.setChatId(message.getChatId());
        if (replyTo) {
            out.setReplyToMessageId(message.getMessageId());
        }
        out.enableHtml(true);
        out.setText(text);
        return execute(out);
    }

    //@Scheduled(fixedDelay = 15000)
    public void sendToChatScheduled() throws TelegramApiException {
        LOGGER.info("sendToChatScheduled() - msg: started job");
        //sendToChat(CHAT_ID, "test");
    }

    @Override
    public String getBotToken() {
        return "5430654397:AAFFfiCrefgArpKkBVBSaAUxu60TgPcGjjs";
    }


    @Override
    public String getBotUsername() {
        return "MemoryGymBot";
    }

    /*
    try{
            String word="〜のそばに";
            word=java.net.URLEncoder.encode(word, "UTF-8");
            URL url = new URL("http://translate.google.com/translate_tts?tl=eng&q="+word);
            HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
            urlConn.addRequestProperty("User-Agent", "Mozilla/4.76");
            InputStream audioSrc = urlConn.getInputStream();
            DataInputStream read = new DataInputStream(audioSrc);
            OutputStream outstream = new FileOutputStream(new File("mysound.mp3"));
            byte[] buffer = new byte[1024];
            int len;
            while ((len = read.read(buffer)) > 0) {
                    outstream.write(buffer, 0, len);
            }
            outstream.close();
        }catch(IOException e){
                   System.out.println(e.getMessage());
        }
     */
}
