package com.f90.telegram.bot.memorygymbot.dto;

import lombok.*;

import javax.validation.constraints.NotEmpty;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
@Builder
public class IrregularVerbDTO implements Dto {

    private static final long serialVersionUID = 5957773961573096761L;

    @NotEmpty
    private String infinitive;
    @NotEmpty
    private String simplePast;
    @NotEmpty
    private String pastParticiple;

}
