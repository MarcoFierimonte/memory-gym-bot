package com.f90.telegram.bot.memorygymbot.bot;

import com.f90.telegram.bot.memorygymbot.model.Word;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.api.objects.webapp.WebAppInfo;

import java.util.ArrayList;
import java.util.List;

public final class KeyboardBuilder {

    private KeyboardBuilder() {
    }

    public static ReplyKeyboardMarkup menuKeyboard() {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(true);

        List<KeyboardRow> keyboard = new ArrayList<>();

        KeyboardRow keyboardRow1 = new KeyboardRow();
        keyboardRow1.add("TEST");
        keyboardRow1.add("LEARN");
        keyboard.add(keyboardRow1);

        KeyboardRow keyboardRow2 = new KeyboardRow();

        KeyboardButton btnAdd = new KeyboardButton();
        btnAdd.setText("ADD");
        btnAdd.setWebApp(WebAppInfo.builder()
                .url("https://memorygymbot.oa.r.appspot.com/v1/memorygymbot/newWord")
                .build());
        keyboardRow2.add(btnAdd);

        KeyboardButton btnDelete = new KeyboardButton();
        btnDelete.setText("DELETE");
        btnDelete.setWebApp(WebAppInfo.builder()
                .url("https://memorygymbot.oa.r.appspot.com/v1/memorygymbot/deleteWord")
                .build());
        keyboardRow2.add(btnDelete);

        keyboard.add(keyboardRow2);

        replyKeyboardMarkup.setKeyboard(keyboard);
        return replyKeyboardMarkup;
    }

    public static InlineKeyboardMarkup doneKeyboard() {
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        List<InlineKeyboardButton> revealsButton = new ArrayList<>();
        revealsButton.add(getButton("DONE", "TEST_DONE"));
        keyboard.add(revealsButton);
        InlineKeyboardMarkup out = new InlineKeyboardMarkup();
        out.setKeyboard(keyboard);
        return out;
    }

    @Deprecated
    public static InlineKeyboardMarkup testWordsKeyboard(List<Word> words) {
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        for (Word currentWord : words) {
            List<InlineKeyboardButton> row = new ArrayList<>();
            row.add(getButton(currentWord.getIta(), currentWord.getIta()));
            keyboard.add(row);
        }
        // add reveals button
        List<InlineKeyboardButton> revealsButton = new ArrayList<>();
        revealsButton.add(getButton("REVEALS", "reveals"));
        keyboard.add(revealsButton);

        InlineKeyboardMarkup out = new InlineKeyboardMarkup();
        out.setKeyboard(keyboard);
        return out;
    }

    private static InlineKeyboardButton getButton(String text, String callback) {
        InlineKeyboardButton button = new InlineKeyboardButton();
        button.setText(text);
        button.setCallbackData(callback);
        return button;
    }
}
