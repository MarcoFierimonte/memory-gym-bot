package com.f90.telegram.bot.memorygymbot.bot.keyboard;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

public final class KeyboardBuilder {

    private KeyboardBuilder() {
    }

    public static ReplyKeyboardMarkup getMainMenuKeyboard() {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);

        List<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow keyboardFirstRow = new KeyboardRow();
        keyboardFirstRow.add("button1");
        keyboardFirstRow.add("button2");
        KeyboardRow keyboardSecondRow = new KeyboardRow();
        keyboardSecondRow.add("button3");
        keyboardSecondRow.add("button4");
        keyboard.add(keyboardFirstRow);
        keyboard.add(keyboardSecondRow);
        replyKeyboardMarkup.setKeyboard(keyboard);

        return replyKeyboardMarkup;
    }

    public static InlineKeyboardMarkup keyboard2() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> listInlineButtons = new ArrayList<>();
        List<InlineKeyboardButton> reportSaleBtn = new ArrayList<>();
        List<InlineKeyboardButton> reportBuyBtn = new ArrayList<>();
        List<InlineKeyboardButton> reportPriceBtn = new ArrayList<>();
        InlineKeyboardButton button1 = new InlineKeyboardButton();
        InlineKeyboardButton button2 = new InlineKeyboardButton();
        InlineKeyboardButton button3 = new InlineKeyboardButton();

        button1.setText("text1");
        button1.setCallbackData("callback text1");

        button2.setText("text2");
        button2.setCallbackData("callback text2");

        button3.setText("text3");
        button3.setCallbackData("callback text3");

        reportSaleBtn.add(button1);
        reportBuyBtn.add(button2);
        reportPriceBtn.add(button3);
        listInlineButtons.add(reportSaleBtn);
        listInlineButtons.add(reportBuyBtn);
        listInlineButtons.add(reportPriceBtn);
        inlineKeyboardMarkup.setKeyboard(listInlineButtons);

        return inlineKeyboardMarkup;
    }
}
