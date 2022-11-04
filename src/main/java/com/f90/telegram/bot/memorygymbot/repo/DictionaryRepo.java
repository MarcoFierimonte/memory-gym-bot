package com.f90.telegram.bot.memorygymbot.repo;

import com.f90.telegram.bot.memorygymbot.model.Word;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface DictionaryRepo extends MongoRepository<Word, String>, WordRepo {

    @Aggregation(pipeline = {
            "{'$match':{'chatId': ?0 }}",
            "{'$match':{'frequency': 0 }}",
            "{$sample:{size: ?1 }}"}
    )
    AggregationResults<Word> random(Long chatId, Integer number);
}
