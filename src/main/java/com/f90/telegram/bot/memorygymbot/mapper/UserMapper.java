package com.f90.telegram.bot.memorygymbot.mapper;

import com.f90.telegram.bot.memorygymbot.dto.UserDTO;
import com.f90.telegram.bot.memorygymbot.dto.WordDTO;
import com.f90.telegram.bot.memorygymbot.model.User;
import com.f90.telegram.bot.memorygymbot.model.Word;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class UserMapper {

    private UserMapper() {
        // empty
    }

    // -- WORD -- //

    public static User toUser(UserDTO in) {
        if (in == null) {
            return null;
        }
        return User.builder()
                .chatId(in.getChatId())
                .testNotificationEnabled(in.isTestNotificationEnabled())
                .userId(in.getUserId())
                .userName(in.getUserName())
                .build();
    }

    public static List<User> toUsers(List<UserDTO> users) {
        final List<User> out;
        if(users != null) {
            out = new ArrayList<>(users.size());
            users.forEach(word -> out.add(toUser(word)));
        } else {
            out = Collections.emptyList();
        }
        return out;
    }

    // -- WORD DTO -- //

    public static UserDTO toUserDTO(User in) {
        if (in == null) {
            return null;
        }
        return UserDTO.builder()
                .chatId(in.getChatId())
                .testNotificationEnabled(in.isTestNotificationEnabled())
                .userId(in.getUserId())
                .userName(in.getUserName())
                .build();
    }

    public static List<UserDTO> toUserDTOs(List<User> words) {
        final List<UserDTO> out;
        if(words != null) {
            out = new ArrayList<>(words.size());
            words.forEach(word -> out.add(toUserDTO(word)));
        } else {
            out = Collections.emptyList();
        }
        return out;
    }
}
