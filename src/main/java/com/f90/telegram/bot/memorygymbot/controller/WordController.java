package com.f90.telegram.bot.memorygymbot.controller;

import com.f90.telegram.bot.memorygymbot.dto.WordDTO;
import com.f90.telegram.bot.memorygymbot.model.Word;
import com.f90.telegram.bot.memorygymbot.service.WordService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/v1/words")
public class WordController {

    private final WordService wordService;

    public WordController(WordService wordService) {
        this.wordService = wordService;
    }

    @ResponseStatus(value = HttpStatus.OK)
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Word> findAll() {
        return wordService.findAll();
    }

    @ResponseStatus(value = HttpStatus.OK)
    @GetMapping(value = "/test", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Word> testDefault() {
        return wordService.test(null);
    }

    @ResponseStatus(value = HttpStatus.OK)
    @GetMapping(value = "/test/{number}", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Word> test(@PathVariable(value = "number", required = false) Integer number) {
        return wordService.test(number);
    }

    @ResponseStatus(value = HttpStatus.OK)
    @DeleteMapping(value = "/ita/{word}", produces = MediaType.APPLICATION_JSON_VALUE)
    public void deleteIta(@PathVariable(value = "word") String word) {
        wordService.delete(word);
    }

    @ResponseStatus(value = HttpStatus.OK)
    @PostMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Word search(@RequestBody WordDTO word) {
        return wordService.add(Word.builder().ita(word.getIta()).eng(word.getEng()).build());
    }
}
