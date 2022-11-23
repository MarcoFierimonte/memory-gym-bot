package com.f90.telegram.bot.memorygymbot.bot;

import com.f90.telegram.bot.memorygymbot.dto.IrregularVerbDTO;
import com.f90.telegram.bot.memorygymbot.dto.WordDTO;
import org.apache.commons.lang3.StringUtils;

public class MessageUtil {

    private static final String TG_SPOILER_START = "<span class='tg-spoiler'> ";
    private static final String TG_SPOILER_END = " </span>";

    private MessageUtil() {
    }

    public static String buildGuessWordText(WordDTO word, Long chatId) {
        String out = EmojiUtil.ITA_FLAG + " <b>" + word.getIta() + "</b>" + EmojiUtil.FINGER_TO_RIGHT + EmojiUtil.ENG_FLAG + TG_SPOILER_START + word.getEng() + TG_SPOILER_END;
        if (StringUtils.isNotEmpty(word.getPronounce())) {
            out = out + EmojiUtil.FINGER_TO_RIGHT + EmojiUtil.VOICE_HEAD + TG_SPOILER_START + word.getPronounce() + TG_SPOILER_END;
        }
        out = out
                + "\n<a href=\"https://memorygymbot.oa.r.appspot.com/v1/words/addFavorite?chatId=" + chatId + "&ita=" + word.getIta() + "\">(ADD⭐)</a> "
                + "<a href=\"https://memorygymbot.oa.r.appspot.com/v1/words/deleteFavorite?chatId=" + chatId + "&ita=" + word.getIta() + "\">(DELETE⛔)</a>";
        return out;
    }

    public static String buildFavoritesWordText(WordDTO word, Long chatId) {
        String out = EmojiUtil.ITA_FLAG + " <b>" + word.getIta() + "</b>" + EmojiUtil.FINGER_TO_RIGHT + EmojiUtil.ENG_FLAG + TG_SPOILER_START + word.getEng() + TG_SPOILER_END;
        if (StringUtils.isNotEmpty(word.getPronounce())) {
            out = out + EmojiUtil.FINGER_TO_RIGHT + EmojiUtil.VOICE_HEAD + TG_SPOILER_START + word.getPronounce() + TG_SPOILER_END;
        }
        out = out
                + "<a href=\"https://memorygymbot.oa.r.appspot.com/v1/words/deleteFavorite?chatId=" + chatId + "&ita=" + word.getIta() + "\">(DELETE⛔)</a>";
        return out;
    }

    public static String buildLearnWordText(WordDTO word) {
        String out = EmojiUtil.ITA_FLAG + " <b>" + word.getIta() + "</b>" + EmojiUtil.FINGER_TO_RIGHT + EmojiUtil.ENG_FLAG + " " + word.getEng();
        if (StringUtils.isNotEmpty(word.getPronounce())) {
            out = out + EmojiUtil.FINGER_TO_RIGHT + EmojiUtil.VOICE_HEAD + word.getPronounce();
        }
        return out;
    }

    public static String buildGuessVerbText(IrregularVerbDTO verb) {
        return EmojiUtil.BRAIN + " <b>Infinite:</b> " + verb.getInfinitive()
                + EmojiUtil.FINGER_TO_RIGHT + "Simple Past:" + TG_SPOILER_START + verb.getSimplePast() + TG_SPOILER_END
                + EmojiUtil.FINGER_TO_RIGHT + "Past Participle:" + TG_SPOILER_START + verb.getPastParticiple() + TG_SPOILER_END;
    }
}
