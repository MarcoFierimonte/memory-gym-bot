package com.f90.telegram.bot.memorygymbot.bot;

import com.f90.telegram.bot.memorygymbot.dto.IrregularVerbDTO;
import com.f90.telegram.bot.memorygymbot.dto.WordDTO;
import org.apache.commons.lang3.StringUtils;

public class MessageUtil {

    private static final String TG_SPOILER_START = "<span class='tg-spoiler'> ";
    private static final String TG_SPOILER_END = " </span>";

    private MessageUtil() {
    }

    public static String buildGuessWordText(WordDTO word) {
        String out = EmojiUtil.ITA_FLAG + " <b>" + word.getIta() + "</b> " + EmojiUtil.FINGER_TO_RIGHT + " " + EmojiUtil.ENG_FLAG + TG_SPOILER_START + word.getEng() + TG_SPOILER_END;
        if (StringUtils.isNotEmpty(word.getPronounce())) {
            out = out + " " + EmojiUtil.FINGER_TO_RIGHT + EmojiUtil.VOICE_HEAD + TG_SPOILER_START + word.getPronounce() + TG_SPOILER_END;
        }
        return out;
    }

    public static String buildLearnWordText(WordDTO word) {
        String out = EmojiUtil.ITA_FLAG + " <b>" + word.getIta() + "</b> " + EmojiUtil.FINGER_TO_RIGHT + " " + EmojiUtil.ENG_FLAG + " " + word.getEng();
        if (StringUtils.isNotEmpty(word.getPronounce())) {
            out = out + " " + EmojiUtil.FINGER_TO_RIGHT + EmojiUtil.VOICE_HEAD + word.getPronounce();
        }
        return out;
    }

    public static String buildGuessVerbText(IrregularVerbDTO verb) {
        return "INFINITE: <b>" + verb.getInfinitive() + "</b> "
                + EmojiUtil.FINGER_TO_RIGHT + " SIMPLE PAST:" + TG_SPOILER_START + verb.getSimplePast() + TG_SPOILER_END
                + EmojiUtil.FINGER_TO_RIGHT + " PAST PARTICIPLE:" + TG_SPOILER_START + verb.getPastParticiple() + TG_SPOILER_END;
    }
}
