package com.f90.telegram.bot.memorygymbot.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("init_dataset")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Setter
public class InitDatasetWord {

    @Id
    private String id;

    private String eng;
    private String ita;
    private String pronounce;

    public InitDatasetWord(String eng, String ita) {
        this.eng = eng;
        this.ita = ita;
    }

    public InitDatasetWord(String eng, String ita, String pronounce) {
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

}
