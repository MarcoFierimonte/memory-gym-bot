package com.f90.telegram.bot.memorygymbot.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@ToString
public class UserDTO implements Dto {

    private static final long serialVersionUID = -1109278781348767792L;

    private Long chatId;
    private Long userId;
    private String userName;
    private boolean testNotificationEnabled;

}
