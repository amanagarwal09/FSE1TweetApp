package com.tweetapp.service;


import com.tweetapp.entity.IdSequence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class SequenceServiceImpl implements SequenceService {

    @Autowired
    private MongoOperations mongoOperations;

    /**
     * Generates custom sequences
     *
     * @param sequenceName
     * @return Integer
     */
    public Integer getNextSequence(String sequenceName) {
        Query query = new Query(Criteria.where("id").is(sequenceName));
        Update update = new Update().inc("sequence", 1);
        IdSequence sequenceCounter = mongoOperations.findAndModify(query, update,
                FindAndModifyOptions.options().returnNew(true).upsert(true), IdSequence.class);
        return Objects.nonNull(sequenceCounter) ? sequenceCounter.getSequence() : 1;
    }
}