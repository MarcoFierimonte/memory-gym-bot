package com.f90.telegram.bot.memorygymbot.repo;

import com.f90.telegram.bot.memorygymbot.model.IrregularVerb;
import com.f90.telegram.bot.memorygymbot.model.Word;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface IrregularVerbRepo extends MongoRepository<IrregularVerb, String> {

}
