package com.f90.telegram.bot.memorygymbot.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@ToString
public class UserDTO {

    private Long chatId;
    private Long userId;
    private String userName;
    private boolean testNotificationEnabled;

}
