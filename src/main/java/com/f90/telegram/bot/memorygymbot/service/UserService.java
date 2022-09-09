package com.f90.telegram.bot.memorygymbot.service;

import com.f90.telegram.bot.memorygymbot.model.User;
import com.f90.telegram.bot.memorygymbot.repo.UserRepo;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

@Component
@RequiredArgsConstructor
public class UserService {

    private final UserRepo userRepo;

    public String getUserName(Update update) {
        String userName = update.getMessage().getFrom().getUserName();
        if (StringUtils.isEmpty(userName)) {
            userName = update.getMessage().getFrom().getFirstName() + "/" + update.getMessage().getFrom().getLastName();
        }
        return userName;
    }

    public User save(User user) {
        return userRepo.save(user);
    }

    public User findByChatId(Long chatId) {
        return userRepo.findByChatId(chatId);
    }

    public List<User> findAll(User input) {
        Example<User> query = Example.of(User.builder()
                .chatId(input.getChatId())
                .userName(input.getUserName())
                .lastTestPending(input.isLastTestPending())
                .build());
        return userRepo.findAll(query);
    }
}
