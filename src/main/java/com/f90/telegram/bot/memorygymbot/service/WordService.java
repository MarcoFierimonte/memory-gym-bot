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
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class WordService {

    private final InitDatasetRepo initDatasetRepo;
    private final WordRepo wordRepo;
    private final DictionaryRepo dictionaryRepo;

    public List<WordDTO> findAll(Long chatId) {
        return WordMapper.toWordDTOs(wordRepo.findAll(Word.builder()
                .chatId(chatId)
                .build()));
    }

    public List<WordDTO> findAllFavorites(Long chatId) {
        return WordMapper.toWordDTOs(wordRepo.findAll(Word.builder()
                .chatId(chatId)
                .favorite(1)
                .build()));
    }

    public WordDTO find(Word input) {
        return WordMapper.toWordDTO(wordRepo.findOne(Word.builder()
                .chatId(input.getChatId())
                .ita(input.getIta())
                .eng(input.getEng())
                .build()));
    }

    public WordDTO add(Word newWord) {
        Word updatedWord;
        if (newWord != null && newWord.getChatId() != null) {
            updatedWord = wordRepo.update(newWord);
        } else {
            throw new InternalException("add() - msg: missing mandatory params.");
        }
        return WordMapper.toWordDTO(updatedWord);
    }

    public List<WordDTO> test(Long chatId, Integer wordsToGuessNumber) {
        Integer wordsToGuess = Objects.requireNonNullElse(wordsToGuessNumber, 4);
        List<Word> extractedWords = dictionaryRepo.random(chatId, wordsToGuess).getMappedResults();
        if (extractedWords.size() == wordsToGuess) {
            // set words as "extracted"
            extractedWords.forEach(current ->
            {
                current.setFrequency(1);
                wordRepo.update(current);
            });
        } else {
            wordRepo.restoreFrequencies(chatId);
            extractedWords = dictionaryRepo.random(chatId, wordsToGuess).getMappedResults();
        }

        return WordMapper.toWordDTOs(extractedWords);
    }

    public void delete(Word input) {
        if (input.getChatId() == null) {
            throw new InternalException("add() - msg: missing mandatory 'chatId' param.");
        }
        wordRepo.deleteEntry(input);
    }

    public void addFavorite(Long chatId, String ita) {
        updateFavorite(chatId, ita, true);
    }

    public void deleteFavorite(Long chatId, String ita) {
        updateFavorite(chatId, ita, false);
    }

    public void updateFavorite(Long chatId, String ita, boolean add) {
        Word word = wordRepo.findOne(Word.builder()
                .chatId(chatId)
                .ita(ita)
                .build());
        if (word != null) {
            word.setFavorite(add ? 1 : 0);
            wordRepo.update(word);
        } else {
            throw new InternalException("Favorite word='" + ita + "' not found. ChatId=" + chatId);
        }
    }

    public void init(Long chatId) {
        List<InitDatasetWord> initDatasetWords = initDatasetRepo.findAll();
        for (InitDatasetWord initWord : initDatasetWords) {
            wordRepo.update(Word.builder()
                    .chatId(chatId)
                    .ita(initWord.getIta())
                    .eng(initWord.getEng())
                    .pronounce(initWord.getPronounce())
                    .frequency(0)
                    .build());
        }
    }

}
