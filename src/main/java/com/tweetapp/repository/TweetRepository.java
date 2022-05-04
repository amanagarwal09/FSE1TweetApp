package com.tweetapp.repository;

import com.tweetapp.entity.TweetEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TweetRepository extends MongoRepository<TweetEntity, Integer> {

    List<TweetEntity> findByUserId(Integer userId);
}