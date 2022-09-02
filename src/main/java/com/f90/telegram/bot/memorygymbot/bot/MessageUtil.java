package com.f90.telegram.bot.memorygymbot.bot;

import com.f90.telegram.bot.memorygymbot.model.Word;

public class MessageUtil {

    private MessageUtil() {
    }

    public static String buildGuessWordText(Word word) {
        return EmojiUtil.ITA_FLAG + " <b>" + word.getIta() + "</b>     " + EmojiUtil.FINGER_TO_RIGHT + " " + EmojiUtil.ENG_FLAG + "<span class='tg-spoiler'> " + word.getEng() + " </span>";
    }

    public static String buildLearnWordText(Word word) {
        return EmojiUtil.ITA_FLAG + " <b>" + word.getIta() + "</b>     " + EmojiUtil.FINGER_TO_RIGHT + " " + EmojiUtil.ENG_FLAG + " " + word.getEng();
    }
}
