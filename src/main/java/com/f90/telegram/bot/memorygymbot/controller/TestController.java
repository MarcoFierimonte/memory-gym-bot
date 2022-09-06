package com.f90.telegram.bot.memorygymbot.controller;

import com.f90.telegram.bot.memorygymbot.dto.UserDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@Slf4j
public class TestController {

    @GetMapping("/addUser")
    public String sendForm(UserDTO user) {
        log.info("sendForm() - msg: user={}", user);
        return "addUser";
    }

    @PostMapping("/addUser")
    public String processForm(UserDTO user) {
        log.info("processForm() - msg: user={}", user);
        return "showMessage";
    }
}
