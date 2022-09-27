package com.f90.telegram.bot.memorygymbot.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("user")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@ToString
public class User {

    @Id
    private Long chatId;

    private Long userId;
    private String userName;
    private boolean testNotificationEnabled;

}
