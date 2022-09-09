package com.f90.telegram.bot.memorygymbot.mapper;

import com.f90.telegram.bot.memorygymbot.dto.WordDTO;
import com.f90.telegram.bot.memorygymbot.model.Word;

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
        return Word.builder()
                .ita(in.getIta())
                .eng(in.getEng())
                .pronounce(in.getPronounce())
                .build();
    }

    public static List<Word> toWords(List<WordDTO> words) {
        final List<Word> out;
        if(words != null) {
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
        return WordDTO.builder()
                .ita(in.getIta())
                .eng(in.getEng())
                .pronounce(in.getPronounce())
                .build();
    }

    public static List<WordDTO> toWordDTOs(List<Word> words) {
        final List<WordDTO> out;
        if(words != null) {
            out = new ArrayList<>(words.size());
            words.forEach(word -> out.add(toWordDTO(word)));
        } else {
            out = Collections.emptyList();
        }
        return out;
    }
}
