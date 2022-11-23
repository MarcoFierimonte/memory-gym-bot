package com.f90.telegram.bot.memorygymbot.repo;

import com.f90.telegram.bot.memorygymbot.model.InitDatasetWord;
import com.f90.telegram.bot.memorygymbot.model.Word;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface InitDatasetRepo extends MongoRepository<InitDatasetWord, String> {

}
