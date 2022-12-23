package com.f90.telegram.bot.memorygymbot.repo;

import com.f90.telegram.bot.memorygymbot.model.Word;
import com.f90.telegram.bot.memorygymbot.util.JsonUtil;
import com.mongodb.Block;
import com.mongodb.client.AggregateIterable;
import org.apache.commons.lang3.StringUtils;
import org.bson.Document;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static com.f90.telegram.bot.memorygymbot.model.Word.*;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;
import static org.springframework.data.mongodb.core.query.Criteria.where;

@Component
public class WordRepoImpl implements WordRepo {

    private final MongoOperations mongoOperations;

    public WordRepoImpl(MongoOperations mongoOperations) {
        this.mongoOperations = mongoOperations;
    }

    @Override
    public List<Word> findAll(Word word) {
        return mongoOperations.find(buildQuery(word), Word.class);
    }

    @Override
    public Word findOne(Word word) {
        return mongoOperations.findOne(buildQuery(word), Word.class);
    }

    public Word update(Word word) {
        Query findQuery = new Query();
        findQuery.addCriteria(where(CHAT_ID_FIELD).is(word.getChatId()));
        if (StringUtils.isNotEmpty(word.getIta())) {
            findQuery.addCriteria(where(ITA_FIELD).is(word.getIta()));
        }

        Update updateFields = new Update();
        updateFields.set(ITA_FIELD, word.getIta());
        updateFields.set(ENG_FIELD, word.getEng());
        updateFields.set(PRONOUNCE_FIELD, word.getPronounce());
        updateFields.set(FREQUENCY_FIELD, word.getFrequency() != null ? word.getFrequency() : 0);
        updateFields.set(FAVORITE_FIELD, word.getFavorite() != null ? word.getFavorite() : 0);
        updateFields.set("lastUpdate", new Date());

        mongoOperations.upsert(findQuery, updateFields, Word.class);

        return mongoOperations.findOne(findQuery, Word.class);
    }

    public void restoreFrequencies(Long chatId) {
        Document query = new Document();
        query.append(CHAT_ID_FIELD, chatId)
                .append(FREQUENCY_FIELD, 1);

        Document setData = new Document();
        setData.append(FREQUENCY_FIELD, 0);
        Document update = new Document();
        update.append("$set", setData);

        mongoOperations.getCollection("dictionary").updateMany(query, update);
    }

    @Override
    public void deleteEntry(Word word) {
        Query findQuery = new Query();
        findQuery.addCriteria(where(ITA_FIELD).is(word.getIta()));
        findQuery.addCriteria(where(CHAT_ID_FIELD).is(word.getChatId()));
        mongoOperations.findAndRemove(findQuery, Word.class);
    }

//    @Override
//    public List<Word> random(Long chatId, Integer number) {
//        Aggregation aggregation = Aggregation.newAggregation(
//                match(where(CHAT_ID_FIELD).is(chatId)),
//                match(where(FREQUENCY_FIELD).is(0))
////                ,limit(number)
//        );
//        return mongoOperations.aggregate(aggregation, "dictionary", Word.class)
//                .getMappedResults();
//    }

    private Query buildQuery(Word word) {
        Query query = new Query();
        if (word.getChatId() != null) {
            query.addCriteria(where(CHAT_ID_FIELD).is(word.getChatId()));
        }
        if (StringUtils.isNotEmpty(word.getIta())) {
            query.addCriteria(where(ITA_FIELD).is(word.getIta()));
        }
        if (StringUtils.isNotEmpty(word.getEng())) {
            query.addCriteria(where(ENG_FIELD).is(word.getEng()));
        }
        if (StringUtils.isNotEmpty(word.getPronounce())) {
            query.addCriteria(where(PRONOUNCE_FIELD).is(word.getPronounce()));
        }
        if (word.getFrequency() != null) {
            query.addCriteria(where(FREQUENCY_FIELD).is(word.getFrequency()));
        }
        if (word.getFavorite() != null) {
            query.addCriteria(where(FAVORITE_FIELD).is(word.getFavorite()));
        }
        return query;
    }


}
