package com.f90.telegram.bot.memorygymbot.service;

import com.f90.telegram.bot.memorygymbot.dto.WordDTO;
import com.f90.telegram.bot.memorygymbot.exception.InternalException;
import com.f90.telegram.bot.memorygymbot.mapper.WordMapper;
import com.f90.telegram.bot.memorygymbot.model.Word;
import com.f90.telegram.bot.memorygymbot.repo.DictionaryRepo;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Component
public class WordService {

    private final DictionaryRepo dictionaryRepo;

    public WordService(DictionaryRepo dictionaryRepo) {
        this.dictionaryRepo = dictionaryRepo;
    }

    public List<WordDTO> findAll(Long chatId) {
        Example<Word> query = Example.of(Word.builder().chatId(chatId).build());
        return WordMapper.toWordDTOs(dictionaryRepo.findAll(query));
    }

    public WordDTO find(Word input) {
        Example<Word> query = Example.of(Word.builder()
                .chatId(input.getChatId())
                .ita(input.getIta())
                .eng(input.getEng())
                .build());
        return WordMapper.toWordDTO(dictionaryRepo.findOne(query).orElse(null));
    }

    public WordDTO add(Word newWord) {
        Word updatedWord;
        if (newWord != null && newWord.getChatId() != null) {
            updatedWord = dictionaryRepo.update(newWord);
        } else {
            throw new InternalException("add() - msg: missing mandatory params.");
        }
        return WordMapper.toWordDTO(updatedWord);
    }

    public List<WordDTO> test(Long chatId, Integer wordsToGuessNumber) {
        return WordMapper.toWordDTOs(
                dictionaryRepo.random(chatId, Objects.requireNonNullElse(wordsToGuessNumber, 5)).getMappedResults()
        );
    }

    public void delete(Word input) {
        if(input.getChatId() == null) {
            throw new InternalException("add() - msg: missing mandatory 'chatId' param.");
        }
        dictionaryRepo.deleteEntry(input);
    }
}
