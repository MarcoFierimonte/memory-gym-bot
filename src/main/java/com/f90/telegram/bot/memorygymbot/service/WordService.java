package com.f90.telegram.bot.memorygymbot.service;

import com.f90.telegram.bot.memorygymbot.dto.WordDTO;
import com.f90.telegram.bot.memorygymbot.exception.InternalException;
import com.f90.telegram.bot.memorygymbot.mapper.WordMapper;
import com.f90.telegram.bot.memorygymbot.model.InitDatasetWord;
import com.f90.telegram.bot.memorygymbot.model.Word;
import com.f90.telegram.bot.memorygymbot.repo.DictionaryRepo;
import com.f90.telegram.bot.memorygymbot.repo.InitDatasetRepo;
import com.f90.telegram.bot.memorygymbot.repo.WordRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class WordService {

    private final DictionaryRepo dictionaryRepo;
    private final InitDatasetRepo initDatasetRepo;

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
        Integer wordsToGuess = Objects.requireNonNullElse(wordsToGuessNumber, 4);
        List<Word> extractedWords = dictionaryRepo.random(chatId, wordsToGuess).getMappedResults();
        if(extractedWords.size() == wordsToGuess) {
            // set words as "extracted"
            extractedWords.forEach(current ->
            {
                current.setFrequency(1);
                dictionaryRepo.update(current);
            });
        } else {
            dictionaryRepo.restoreFrequencies(chatId);
            extractedWords = dictionaryRepo.random(chatId, wordsToGuess).getMappedResults();
        }

        return WordMapper.toWordDTOs(extractedWords);
    }

    public void delete(Word input) {
        if (input.getChatId() == null) {
            throw new InternalException("add() - msg: missing mandatory 'chatId' param.");
        }
        dictionaryRepo.deleteEntry(input);
    }

    public void init(Long chatId) {
        List<InitDatasetWord> initDatasetWords = initDatasetRepo.findAll();
        for (InitDatasetWord initWord : initDatasetWords) {
            dictionaryRepo.update(Word.builder()
                    .chatId(chatId)
                    .ita(initWord.getIta())
                    .eng(initWord.getEng())
                    .pronounce(initWord.getPronounce())
                    .build());
        }
    }

}
