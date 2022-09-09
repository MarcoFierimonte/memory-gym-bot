package com.f90.telegram.bot.memorygymbot.controller;

import com.f90.telegram.bot.memorygymbot.dto.WordDTO;
import com.f90.telegram.bot.memorygymbot.model.Word;
import com.f90.telegram.bot.memorygymbot.service.WordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/v1/words")
@Slf4j
public class WordController {

    private final WordService wordService;

    public WordController(WordService wordService) {
        this.wordService = wordService;
    }

    @ResponseStatus(value = HttpStatus.OK)
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<WordDTO> findAll(@RequestParam(name = "chatId", required = false) Long chatId) {
        return wordService.findAll(chatId);
    }

    @ResponseStatus(value = HttpStatus.OK)
    @GetMapping(value = "/test", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<WordDTO> testDefault(@RequestParam(name = "chatId", required = false) Long chatId) {
        return wordService.test(chatId, null);
    }

    @ResponseStatus(value = HttpStatus.OK)
    @GetMapping(value = "/test/{number}", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<WordDTO> test(@PathVariable(value = "number") Integer number,
                              @RequestParam(name = "chatId") Long chatId) {
        return wordService.test(chatId, number);
    }

    @ResponseStatus(value = HttpStatus.OK)
    @PostMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public WordDTO addWord(@RequestBody @Valid WordDTO word) {
        return wordService.add(Word.builder()
                .ita(word.getIta())
                .eng(word.getEng())
                .pronounce(word.getPronounce())
                .chatId(word.getChatId())
                .build());
    }

    @ResponseStatus(value = HttpStatus.OK)
    @DeleteMapping(value = "/ita/{word}", produces = MediaType.APPLICATION_JSON_VALUE)
    public void deleteIta(@PathVariable(value = "word") String word,
                          @RequestParam(name = "chatId") Long chatId) {
        wordService.delete(Word.builder()
                .chatId(chatId)
                .ita(word)
                .build());
    }

}
