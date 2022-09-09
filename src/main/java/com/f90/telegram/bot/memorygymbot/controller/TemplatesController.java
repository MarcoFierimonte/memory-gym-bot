package com.f90.telegram.bot.memorygymbot.controller;

import com.f90.telegram.bot.memorygymbot.dto.WordDTO;
import com.f90.telegram.bot.memorygymbot.mapper.WordMapper;
import com.f90.telegram.bot.memorygymbot.model.Word;
import com.f90.telegram.bot.memorygymbot.service.WordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(path = "/v1/memorygymbot")
@Slf4j
public class TemplatesController {

    private final WordService wordService;

    public TemplatesController(WordService wordService) {
        this.wordService = wordService;
    }

    @GetMapping("/home")
    public String viewHomePage() {
        log.info("viewHomePage() - msg");
        return "index";
    }

    @GetMapping("/done")
    public String operationDonePage() {
        log.info("operationDonePage() - msg");
        return "done";
    }

    @GetMapping("/newWord")
    public String addWordPage(@RequestParam(value = "chatId") Long chatId,
                              Model model) {
        log.info("addWordPage() - msg: called /newWord");
        WordDTO word = new WordDTO();
        word.setChatId(chatId);
        model.addAttribute("word", word);
        return "add-word";
    }

    @GetMapping("/deleteWord")
    public String deleteWordPage(@RequestParam(value = "chatId") Long chatId,
                                 Model model) {
        log.info("deleteWordPage() - msg: called /deleteWord");
        WordDTO word = new WordDTO();
        word.setChatId(chatId);
        model.addAttribute("word", word);
        return "delete-word";
    }

    @PostMapping("/words/add")
    public String addNewWord(@ModelAttribute("word") WordDTO word) {
        log.info("addNewWord() - msg: new word={}", word);
        wordService.add(Word.builder()
                .ita(word.getIta())
                .eng(word.getEng())
                .chatId(word.getChatId())
                .build());
        return "redirect:/v1/memorygymbot/done";
    }

    @PostMapping("/words/delete")
    public String deleteWord(@ModelAttribute("word") WordDTO word) {
        log.info("deleteWord() - msg:  word to delete={}", word);
        wordService.delete(WordMapper.toWord(word));
        return "redirect:/v1/memorygymbot/done";
    }
}
