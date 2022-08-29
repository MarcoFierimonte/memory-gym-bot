package com.f90.telegram.bot.memorygymbot.service;

import com.f90.telegram.bot.memorygymbot.bot.Command;
import com.f90.telegram.bot.memorygymbot.dto.WordDTO;
import com.f90.telegram.bot.memorygymbot.exception.InternalException;
import com.f90.telegram.bot.memorygymbot.model.Word;
import com.f90.telegram.bot.memorygymbot.repo.DictionaryRepo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

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

    /**
     * Example of use: /add tin; lattina
     *
     * @param commandValue the chat command value
     */
    public void add(String commandValue) {
        if (StringUtils.isNotEmpty(commandValue)) {
            String[] splitted = commandValue.split(",");
            String eng = splitted[0].strip();
            String ita = splitted[1].strip();
            Word newWord = new Word(eng, ita);
            dictionaryRepo.save(newWord);
        } else {
            LOGGER.warn("add() - msg: missing value from user.");
        }
    }

    public Word add(WordDTO newWord) {
        Word insertWord;
        if (newWord != null) {
            Word word = new Word(newWord.getEng(), newWord.getIta());
            insertWord = dictionaryRepo.save(word);
        } else {
            throw  new InternalException("add() - msg: missing newWord from user.");
        }
        return insertWord;
    }

    /**
     * Return a list of ITA words to translate. By default, 5 words, but you can specify a custom number from 1 to 10.
     * Example of use:
     * - /test
     * - /test 3 (any number from 1 to 10)
     *
     * @param commandValue the chat command value
     */
    public List<Word> test(String commandValue) {
        List<Word> randomWords;
        if (StringUtils.isNotEmpty(commandValue)) {
            randomWords = dictionaryRepo.random(Integer.parseInt(commandValue)).getMappedResults();
        } else {
            randomWords = dictionaryRepo.random(5).getMappedResults();
        }
        return randomWords;
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
