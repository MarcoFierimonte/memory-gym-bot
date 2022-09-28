package com.f90.telegram.bot.memorygymbot.controller;

import com.f90.telegram.bot.memorygymbot.bot.MemoryGymBotExecutor;
import com.f90.telegram.bot.memorygymbot.dto.WordDTO;
import com.f90.telegram.bot.memorygymbot.mapper.WordMapper;
import com.f90.telegram.bot.memorygymbot.model.Word;
import com.f90.telegram.bot.memorygymbot.service.WordService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/v1/words")
@RequiredArgsConstructor
@Slf4j
public class WordController {

    private final WordService wordService;
    private final MemoryGymBotExecutor memoryGymBotExecutor;

    /**
     * Find all words.
     *
     * @param chatId the chatId
     * @return the words
     */
    @ResponseStatus(value = HttpStatus.OK)
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<WordDTO> findAll(@RequestParam(name = "chatId", required = false) Long chatId) {
        return wordService.findAll(chatId);
    }

    /**
     * Send a 'test' notification to the specified chatId.
     *
     * @param chatId the chatId
     * @return the words
     */
    @ResponseStatus(value = HttpStatus.OK)
    @GetMapping(value = "/test", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<WordDTO> testDefault(@RequestParam(name = "chatId", required = false) Long chatId) {
        return wordService.test(chatId, null);
    }

    /**
     * Send a 'test' notification to the specified chatId.
     *
     * @param number the number of words to ask in the test
     * @param chatId the chatId
     * @return the words
     */
    @ResponseStatus(value = HttpStatus.OK)
    @GetMapping(value = "/test/{number}", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<WordDTO> test(@PathVariable(value = "number") Integer number,
                              @RequestParam(name = "chatId") Long chatId) {
        return wordService.test(chatId, number);
    }

    /**
     * Add a new word.
     *
     * @param wordDTO the word to add
     * @return the word added
     */
    @ResponseStatus(value = HttpStatus.OK)
    @PostMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public WordDTO addWord(@RequestBody @Valid WordDTO wordDTO) {
        return wordService.add(WordMapper.toWord(wordDTO));
    }

    /**
     * Delete an ita word.
     *
     * @param word   the ita word
     * @param chatId the chatId
     */
    @ResponseStatus(value = HttpStatus.OK)
    @DeleteMapping(value = "/ita/{word}", produces = MediaType.APPLICATION_JSON_VALUE)
    public void deleteIta(@PathVariable(value = "word") String word,
                          @RequestParam(name = "chatId") Long chatId) {
        wordService.delete(Word.builder()
                .chatId(chatId)
                .ita(word)
                .build());
    }

    /**
     * Trigger a 'test' notification to all users with notifications enabled.
     */
    @ResponseStatus(value = HttpStatus.OK)
    @GetMapping(value = "/trigger")
    public void triggerTests() {
        new Thread(memoryGymBotExecutor::sendTestToAllUsers).start();
    }

    /**
     * Used to sync all words from init dataset to users dataset.
     */
    @ResponseStatus(value = HttpStatus.OK)
    @GetMapping(value = "/init/sync_users")
    public void addWordsToAllUsers() {
        memoryGymBotExecutor.addWordsToAllUsers();
    }

}
