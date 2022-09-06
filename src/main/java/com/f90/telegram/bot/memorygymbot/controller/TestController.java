package com.f90.telegram.bot.memorygymbot.controller;

import com.f90.telegram.bot.memorygymbot.dto.UserDTO;
import com.f90.telegram.bot.memorygymbot.dto.WordDTO;
import com.f90.telegram.bot.memorygymbot.model.Word;
import com.f90.telegram.bot.memorygymbot.service.WordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@Slf4j
public class TestController {

    private final WordService wordService;

    public TestController(WordService wordService) {
        this.wordService = wordService;
    }

    @GetMapping("/")
    public String viewHomePage(Model model) {
        return "index";
    }

    @GetMapping("/addWord")
    public String addNewEmployee(Model model) {
        WordDTO word = new WordDTO();
        model.addAttribute("word", word);
        return "addWord";
    }

    @PostMapping("/save")
    public String saveEmployee(@ModelAttribute("word") WordDTO word) {
        log.info("saveEmployee() - msg: word={}", word);
        wordService.add(Word.builder()
                .ita(word.getIta())
                .eng(word.getEng())
                .chatId(69501949L)
                .build());
        return "redirect:/";
    }
}
