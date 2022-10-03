package com.f90.telegram.bot.memorygymbot.dto;

import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
@Builder
public class WordDTO implements Dto {

    private static final long serialVersionUID = 253853225712145074L;

    @NotEmpty
    private String eng;

    @NotEmpty
    private String ita;

    private String pronounce;

    @NotNull
    private Long chatId;

}

