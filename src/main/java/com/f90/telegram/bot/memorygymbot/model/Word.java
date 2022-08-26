package com.f90.telegram.bot.memorygymbot.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("dictionary")
public class Word {

    @Id
    private String id;
    private String eng;
    private String ita;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEng() {
        return eng;
    }

    public void setEng(String eng) {
        this.eng = eng;
    }

    public String getIta() {
        return ita;
    }

    public void setIta(String ita) {
        this.ita = ita;
    }
}
