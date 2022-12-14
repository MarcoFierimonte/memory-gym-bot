package com.f90.telegram.bot.memorygymbot.service;

import com.f90.telegram.bot.memorygymbot.dto.UserDTO;
import com.f90.telegram.bot.memorygymbot.mapper.UserMapper;
import com.f90.telegram.bot.memorygymbot.model.User;
import com.f90.telegram.bot.memorygymbot.repo.UserRepo;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserService {

    private final UserRepo userRepo;

    public String getUserName(Update update) {
        StringBuilder userIdentifier = new StringBuilder("");
        if (StringUtils.isNotEmpty(update.getMessage().getFrom().getId() + "")) {
            userIdentifier.append(update.getMessage().getFrom().getId());
        }
        if (StringUtils.isNotEmpty(update.getMessage().getFrom().getUserName())) {
            userIdentifier
                    .append("/")
                    .append(update.getMessage().getFrom().getUserName());
        }
        if (StringUtils.isNotEmpty(update.getMessage().getFrom().getFirstName())) {
            userIdentifier
                    .append("/")
                    .append(update.getMessage().getFrom().getFirstName());
        }
        if (StringUtils.isNotEmpty(update.getMessage().getFrom().getLastName())) {
            userIdentifier
                    .append("/")
                    .append(update.getMessage().getFrom().getLastName());
        }
        return userIdentifier.toString();
    }

    public UserDTO save(User user) {
        return UserMapper.toUserDTO(userRepo.save(user));
    }

    public UserDTO findByChatId(Long chatId) {
        return UserMapper.toUserDTO(userRepo.findByChatId(chatId));
    }

    public List<UserDTO> findAll(Optional<User> input) {
        List<User> out;
        Example<User> query;
        if (input.isPresent()) {
            query = Example.of(User.builder()
                    .chatId(input.get().getChatId())
                    .userName(input.get().getUserName())
                    .testNotificationEnabled(input.get().isTestNotificationEnabled())
                    .build());
            out = userRepo.findAll(query);
        } else {
            out = userRepo.findAll();
        }
        return UserMapper.toUserDTOs(out);
    }
}
