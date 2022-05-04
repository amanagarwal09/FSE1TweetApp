package com.tweetapp.service;

import com.tweetapp.model.Tweet;
import com.tweetapp.model.TweetResponse;
import org.springframework.http.ResponseEntity;

public interface TweetService {
    /**
     * To get all tweets
     *
     * @return TweetResponse
     */
    ResponseEntity<TweetResponse> getAllTweets();

    /**
     * To get all tweet based on Username
     *
     * @param userName
     * @return TweetResponse
     */
    ResponseEntity<TweetResponse> getAllTweetsOfUser(String userName);

    /**
     * To update Tweet
     *
     * @param userName
     * @param id
     * @param tweet
     * @return TweetResponse
     */
    ResponseEntity<TweetResponse> updateTweet(String userName, Integer id, Tweet tweet);

    /**
     * To Post new tweet
     *
     * @param userName
     * @param tweet
     * @return TweetResponse
     */
    ResponseEntity<TweetResponse> postNewTweet(String userName, Tweet tweet);

    /**
     * To delete Tweet
     *
     * @param userName
     * @param id
     * @return TweetResponse
     */
    ResponseEntity<TweetResponse> deleteTweet(String userName, Integer id);

    /**
     * To Like/Unlike a Tweet
     *
     * @param userName
     * @param id
     * @return TweetResponse
     */
    ResponseEntity<TweetResponse> likeTweet(String userName, Integer id);

    /**
     * To reply to a Tweet
     *
     * @param userName
     * @param id
     * @param tweet
     * @return TweetResponse
     */
    ResponseEntity<TweetResponse> replyToTweet(String userName, Integer id, Tweet tweet);
}
