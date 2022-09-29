package com.f90.telegram.bot.memorygymbot.controller;

import com.f90.telegram.bot.memorygymbot.dto.UserDTO;
import com.f90.telegram.bot.memorygymbot.dto.WordDTO;
import com.f90.telegram.bot.memorygymbot.mapper.UserMapper;
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

    @GetMapping("/addWord")
    public String addWordPage(@RequestParam(value = "chatId") Long chatId,
                              Model model) {
        WordDTO wordDTO = new WordDTO();
        wordDTO.setChatId(chatId);
        model.addAttribute("word", wordDTO);
        return "add-new-word";
    }

    @GetMapping("/deleteWord")
    public String deleteWordPage(@RequestParam(value = "chatId") Long chatId,
                                 Model model) {
        WordDTO wordDTO = new WordDTO();
        wordDTO.setChatId(chatId);
        model.addAttribute("word", wordDTO);
        return "delete-word";
    }

    @GetMapping("/config")
    public String configsPage(@RequestParam(value = "chatId") Long chatId,
                              Model model) {
        // retrive previous user configs
        UserDTO currentUserConfigs = userService.findByChatId(chatId);
        UserDTO userDTO = new UserDTO();
        userDTO.setChatId(chatId);
        userDTO.setTestNotificationEnabled(currentUserConfigs.isTestNotificationEnabled());
        model.addAttribute("userConfigs", userDTO);
        return "config";
    }

    @PostMapping("/words/add")
    public String addNewWord(@ModelAttribute("word") WordDTO wordDTO) {
        wordService.add(WordMapper.toWord(wordDTO));
        return "index";
    }

    @PostMapping("/words/delete")
    public String deleteWord(@ModelAttribute("word") WordDTO wordDTO) {
        wordService.delete(WordMapper.toWord(wordDTO));
        return "index";
    }

    @PostMapping(value = "/users/config")
    public String upserUser(@ModelAttribute("userConfig") UserDTO userDTO) {
        userService.save(UserMapper.toUser(userDTO));
        return "index";
    }
}
