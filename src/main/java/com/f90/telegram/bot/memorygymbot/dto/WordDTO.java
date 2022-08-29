package com.f90.telegram.bot.memorygymbot.dto;

public class WordDTO {

    private String id;
    private String eng;
    private String ita;

    public WordDTO(String eng, String ita) {
        this.eng = eng;
        this.ita = ita;
    }

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
