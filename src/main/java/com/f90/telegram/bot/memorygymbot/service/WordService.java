package com.f90.telegram.bot.memorygymbot.service;

import com.f90.telegram.bot.memorygymbot.exception.InternalException;
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

    public List<Word> findAll() {
        return dictionaryRepo.findAll();
    }

    public Word findById(String id) {
        return dictionaryRepo.findById(id).orElse(null);
    }

    public Word findByIta(String ita) {
        return dictionaryRepo.findWordByIta(ita);
    }

    public Word add(Word newWord) {
        Word insertWord;
        if (newWord != null) {
            insertWord = dictionaryRepo.save(newWord);
        } else {
            throw new InternalException("add() - msg: missing newWord from user.");
        }
        return insertWord;
    }

    /**
     * Return a list of ITA words to translate. By default, 5 words, but you can specify a custom number from 1 to 10.
     * Example of use:
     * - /test
     * - /test 3 (any number from 1 to 10)
     *
     * @param wordsToGuessNumber the chat command value
     */
    public List<Word> test(Integer wordsToGuessNumber) {
        return dictionaryRepo.random(Objects.requireNonNullElse(wordsToGuessNumber, 5)).getMappedResults();
    }

    /**
     * Example of use: /delete lattina
     *
     * @param word the word to delete
     */
    public void delete(String word) {
        if (StringUtils.isNotEmpty(word)) {
            dictionaryRepo.deleteByIta(word);
        } else {
            LOGGER.warn("delete() - msg: missing value from user.");
        }
    }
}
