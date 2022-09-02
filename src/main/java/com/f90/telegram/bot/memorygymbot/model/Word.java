package com.f90.telegram.bot.memorygymbot.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("dictionary")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class Word {

    @Id
    private String id;
    private String eng;
    private String ita;
    private Long chatId;

    public Word(String eng, String ita) {
        this.eng = eng;
        this.ita = ita;
    }

}
