package com.f90.telegram.bot.memorygymbot.mapper;

import com.f90.telegram.bot.memorygymbot.dto.WordDTO;
import com.f90.telegram.bot.memorygymbot.exception.InternalException;
import com.f90.telegram.bot.memorygymbot.model.Word;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class WordMapper {

    private WordMapper() {
        // empty
    }

    // -- WORD -- //

    public static Word toWord(WordDTO in) {
        if (in == null) {
            return null;
        }
        if(StringUtils.isEmpty(in.getIta()) || StringUtils.isEmpty(in.getEng())) {
            throw new InternalException("Error during 'dto' to 'model' mapping. Missing mandatory fields");
        }
        return Word.builder()
                .ita(in.getIta().toLowerCase())
                .eng(in.getEng().toLowerCase())
                .chatId(in.getChatId())
                .pronounce(in.getPronounce().toLowerCase())
                .frequency(in.getFrequency() != null ? in.getFrequency() : 0)
                .favorite(in.getFavorite() != null ? in.getFavorite() : 0)
                .build();
    }

    public static List<Word> toWords(List<WordDTO> words) {
        final List<Word> out;
        if (words != null) {
            out = new ArrayList<>(words.size());
            words.forEach(word -> out.add(toWord(word)));
        } else {
            out = Collections.emptyList();
        }
        return out;
    }

    // -- WORD DTO -- //

    public static WordDTO toWordDTO(Word in) {
        if (in == null) {
            return null;
        }
        if(StringUtils.isEmpty(in.getIta()) || StringUtils.isEmpty(in.getEng())) {
            throw new InternalException("Error during 'model' to 'dto' mapping. Missing mandatory fields");
        }
        return WordDTO.builder()
                .ita(in.getIta().toLowerCase())
                .eng(in.getEng().toLowerCase())
                .chatId(in.getChatId())
                .pronounce(in.getPronounce().toLowerCase())
                .frequency(in.getFrequency() != null ? in.getFrequency() : 0)
                .favorite(in.getFavorite() != null ? in.getFavorite() : 0)
                .build();
    }

    public static List<WordDTO> toWordDTOs(List<Word> words) {
        final List<WordDTO> out;
        if (words != null) {
            out = new ArrayList<>(words.size());
            words.forEach(word -> out.add(toWordDTO(word)));
        } else {
            out = Collections.emptyList();
        }
        return out;
    }
}
