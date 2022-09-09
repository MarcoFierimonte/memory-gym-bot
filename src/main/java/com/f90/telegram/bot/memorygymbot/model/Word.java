package com.f90.telegram.bot.memorygymbot.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("dictionary")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Setter
public class Word {

    @Id
    private String id;

    private String eng;
    private String ita;
    private String pronounce;

    private Long chatId;

    public Word(String eng, String ita) {
        this.eng = eng;
        this.ita = ita;
    }

    public Word(String eng, String ita, String pronounce) {
        this.eng = eng;
        this.ita = ita;
        this.pronounce = pronounce;
    }

    public String getId() {
        return id;
    }

    public String getEng() {
        return eng != null ? eng.strip().toLowerCase() : null;
    }

    public String getIta() {
        return ita != null ? ita.strip().toLowerCase() : null;
    }

    public String getPronounce() {
        return pronounce != null ? pronounce.strip().toLowerCase() : null;
    }

    public Long getChatId() {
        return chatId;
    }
}
