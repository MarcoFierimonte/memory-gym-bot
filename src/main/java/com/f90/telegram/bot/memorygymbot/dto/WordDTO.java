package com.f90.telegram.bot.memorygymbot.dto;

import lombok.*;

import javax.validation.constraints.NotEmpty;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
@Builder
public class WordDTO {

    @NotEmpty
    private String eng;

    @NotEmpty
    private String ita;

    private String pronounce;

    @NotEmpty
    private Long chatId;

}

