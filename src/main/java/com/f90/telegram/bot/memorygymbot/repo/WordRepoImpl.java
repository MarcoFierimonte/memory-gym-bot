package com.f90.telegram.bot.memorygymbot.repo;

import com.f90.telegram.bot.memorygymbot.model.Word;
import org.apache.commons.lang3.StringUtils;
import org.bson.Document;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import static com.f90.telegram.bot.memorygymbot.model.Word.CHAT_ID_FIELD;
import static com.f90.telegram.bot.memorygymbot.model.Word.FREQUENCY_FIELD;

@Component
public class WordRepoImpl implements WordRepo {

    private final MongoOperations mongoOperations;

    public WordRepoImpl(MongoOperations mongoOperations) {
        this.mongoOperations = mongoOperations;
    }

    public Word update(Word word) {
        Query findQuery = new Query();
        findQuery.addCriteria(Criteria.where(CHAT_ID_FIELD).is(word.getChatId()));
        if (StringUtils.isNotEmpty(word.getIta())) {
            findQuery.addCriteria(Criteria.where("ita").is(word.getIta()));
        }

        Update updateFields = new Update();
        updateFields.set("ita", word.getIta());
        updateFields.set("eng", word.getEng());
        updateFields.set("pronounce", word.getPronounce());
        updateFields.set("frequency", word.getFrequency() != null ? word.getFrequency() : 0);
        updateFields.set("favorite", word.getFavorite() != null ? word.getFavorite() : 0);

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
    public Word deleteEntry(Word word) {
        Query findQuery = new Query();
        findQuery.addCriteria(Criteria.where("ita").is(word.getIta()));
        findQuery.addCriteria(Criteria.where(CHAT_ID_FIELD).is(word.getChatId()));
        return mongoOperations.findAndRemove(findQuery, Word.class);
    }

}
