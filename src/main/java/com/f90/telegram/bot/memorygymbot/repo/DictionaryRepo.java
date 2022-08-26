package com.f90.telegram.bot.memorygymbot.repo;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import static com.mongodb.client.model.Filters.eq;

public class DictionaryRepo {

    public static void connect() {

        String username="memory_gym_bot";
        String pwd="english1234";
        String uri = "mongodb+srv://" + username + ":" + pwd + "@cluster0.n7aqfhp.mongodb.net/?retryWrites=true&w=majority";

        try (MongoClient mongoClient = MongoClients.create(uri)) {

            MongoDatabase database = mongoClient.getDatabase("memory_gym_bot");
            MongoCollection<Document> collection = database.getCollection("dictionary");

            Document doc = collection.find(eq("eng", "tin")).first();
            System.out.println(doc.toJson());
        }
    }
}
