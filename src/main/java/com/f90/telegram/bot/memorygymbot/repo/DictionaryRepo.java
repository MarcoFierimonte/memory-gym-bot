package com.f90.telegram.bot.memorygymbot.repo;

import com.f90.telegram.bot.memorygymbot.model.Word;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface DictionaryRepo extends MongoRepository<Word, String> {

    Long deleteByIta(String ita);

    Word findWordByIta(String ita);

    @Aggregation(pipeline = {"{$sample:{size: ?0 }}"})
    AggregationResults<Word> random(Integer number);
}
