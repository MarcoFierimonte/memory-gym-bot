package com.f90.telegram.bot.memorygymbot.controller;

import com.f90.telegram.bot.memorygymbot.dto.UserDTO;
import com.f90.telegram.bot.memorygymbot.dto.WordDTO;
import com.f90.telegram.bot.memorygymbot.mapper.WordMapper;
import com.f90.telegram.bot.memorygymbot.model.User;
import com.f90.telegram.bot.memorygymbot.model.Word;
import com.f90.telegram.bot.memorygymbot.service.UserService;
import com.f90.telegram.bot.memorygymbot.service.WordService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(path = "/v1/memorygymbot")
@RequiredArgsConstructor
@Slf4j
public class TemplatesController {

    private final WordService wordService;
    private final UserService userService;

    @GetMapping("/home")
    public String viewHomePage() {
        return "index";
    }

    @GetMapping("/done")
    public String operationDonePage() {
        return "done";
    }

    @GetMapping("/newWord")
    public String addWordPage(@RequestParam(value = "chatId") Long chatId,
                              Model model) {
        WordDTO word = new WordDTO();
        word.setChatId(chatId);
        model.addAttribute("word", word);
        return "add-word";
    }

    @GetMapping("/deleteWord")
    public String deleteWordPage(@RequestParam(value = "chatId") Long chatId,
                                 Model model) {
        WordDTO word = new WordDTO();
        word.setChatId(chatId);
        model.addAttribute("word", word);
        return "delete-word";
    }

    @GetMapping("/config")
    public String configsPage(@RequestParam(value = "chatId") Long chatId,
                             Model model) {
        // retrive previous user configs
        User currentUserConfigs = userService.findByChatId(chatId);
        UserDTO user = new UserDTO();
        user.setChatId(chatId);
        user.setNotificationEnabled(currentUserConfigs.isTestNotificationEnabled());
        model.addAttribute("userConfigs", user);
        return "config";
    }

    @PostMapping("/words/add")
    public String addNewWord(@ModelAttribute("word") WordDTO word) {
        wordService.add(Word.builder()
                .ita(word.getIta())
                .eng(word.getEng())
                .chatId(word.getChatId())
                .build());
        return "done";
    }

    @PostMapping("/words/delete")
    public String deleteWord(@ModelAttribute("word") WordDTO word) {
        wordService.delete(WordMapper.toWord(word));
        return "done";
    }

    @PostMapping(value = "/users/add")
    public String upserUser(@ModelAttribute("userConfig") UserDTO userDTO) {
        userService.save(User.builder()
                .chatId(userDTO.getChatId())
                .testNotificationEnabled(userDTO.isNotificationEnabled())
                .build());
        return "done";
    }
}
