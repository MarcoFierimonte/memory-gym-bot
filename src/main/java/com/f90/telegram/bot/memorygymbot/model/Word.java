package com.f90.telegram.bot.memorygymbot.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("dictionary")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Setter
@Getter
@ToString
public class Word {

    public static final String CHAT_ID_FIELD = "chatId";
    public static final String FREQUENCY_FIELD = "frequency";
    public static final String FAVORITE_FIELD = "favorite";
    public static final String ITA_FIELD = "ita";
    public static final String ENG_FIELD = "eng";
    public static final String PRONOUNCE_FIELD = "pronounce";

    @Id
    private String id;
    private Long chatId;
    private Integer frequency;
    private Integer favorite;

    private String eng;
    private String ita;
    private String pronounce;


    public Word(String eng, String ita) {
        this.eng = eng;
        this.ita = ita;
    }

    public Word(String eng, String ita, String pronounce) {
        this.eng = eng;
        this.ita = ita;
        this.pronounce = pronounce;
    }

}
