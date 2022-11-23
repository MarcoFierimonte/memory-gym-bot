package com.f90.telegram.bot.memorygymbot.repo;

import com.f90.telegram.bot.memorygymbot.model.Word;

import java.util.List;

public interface WordRepo {

//    List<Word> random(Long chatId, Integer number);

    Word update(Word word);

    void deleteEntry(Word word);

    void restoreFrequencies(Long chatId);

    List<Word> findAll(Word query);

    Word findOne(Word query);
}
