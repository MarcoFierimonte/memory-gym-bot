package com.f90.telegram.bot.memorygymbot.repo;

import com.f90.telegram.bot.memorygymbot.model.Word;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

@Component
public class WordRepoImpl implements WordRepo {

    private final MongoOperations mongoOperations;

    public WordRepoImpl(MongoOperations mongoOperations) {
        this.mongoOperations = mongoOperations;
    }

    public Word update(Word word) {
        Query findQuery = new Query();
        findQuery.addCriteria(Criteria.where("ita").is(word.getIta()));

        Update updateFields = new Update();
        updateFields.set("ita", word.getIta());
        updateFields.set("eng", word.getEng());

        return mongoOperations.findAndModify(
                findQuery,
                updateFields,
                new FindAndModifyOptions().returnNew(true),
                Word.class);
    }

}
