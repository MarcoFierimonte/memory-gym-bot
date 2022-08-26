package com.f90.telegram.bot.memorygymbot.repo;

import com.f90.telegram.bot.memorygymbot.model.Word;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

import static com.mongodb.client.model.Filters.eq;

public interface DictionaryRepo extends MongoRepository<Word, String> {

    @Override
    List<Word> findAll();
}
