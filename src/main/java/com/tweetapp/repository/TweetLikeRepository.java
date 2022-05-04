package com.tweetapp.repository;

import com.tweetapp.entity.TweetLikeEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TweetLikeRepository extends MongoRepository<TweetLikeEntity, Integer> {

    Optional<TweetLikeEntity> findByUserIdAndTweetId(Integer userId, Integer tweetId);

    List<TweetLikeEntity> findByTweetId(Integer tweetId);


}