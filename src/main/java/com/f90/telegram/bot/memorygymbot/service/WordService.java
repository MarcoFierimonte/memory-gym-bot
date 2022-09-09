package com.f90.telegram.bot.memorygymbot.service;

import com.f90.telegram.bot.memorygymbot.dto.WordDTO;
import com.f90.telegram.bot.memorygymbot.exception.InternalException;
import com.f90.telegram.bot.memorygymbot.mapper.WordMapper;
import com.f90.telegram.bot.memorygymbot.model.Word;
import com.f90.telegram.bot.memorygymbot.repo.DictionaryRepo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Component
public class WordService {

    private static final Logger LOGGER = LoggerFactory.getLogger(WordService.class);

    private final DictionaryRepo dictionaryRepo;

    public WordService(DictionaryRepo dictionaryRepo) {
        this.dictionaryRepo = dictionaryRepo;
    }

    public List<WordDTO> findAll() {
        return WordMapper.toWordDTOs(dictionaryRepo.findAll());
    }

    public WordDTO findById(String id) {
        return WordMapper.toWordDTO(dictionaryRepo.findById(id).orElse(null));
    }

    public WordDTO findByIta(String ita) {
        return WordMapper.toWordDTO(dictionaryRepo.findWordByIta(ita));
    }

    public WordDTO add(Word newWord) {
        Word updatedWord;
        if (newWord != null) {
            updatedWord = dictionaryRepo.update(newWord);
        } else {
            throw new InternalException("add() - msg: missing newWord from user.");
        }
        return WordMapper.toWordDTO(updatedWord);
    }

    public List<WordDTO> test(Integer wordsToGuessNumber) {
        return WordMapper.toWordDTOs(dictionaryRepo.random(Objects.requireNonNullElse(wordsToGuessNumber, 5)).getMappedResults());
    }

    public void deleteByIta(String word) {
        if (StringUtils.isNotEmpty(word)) {
            dictionaryRepo.deleteByIta(word);
        } else {
            LOGGER.warn("delete() - msg: missing value from user.");
        }
    }
}
