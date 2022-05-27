package com.tweetapp.service;

import com.tweetapp.model.Tweet;
import com.tweetapp.model.TweetResponse;
import org.springframework.http.ResponseEntity;

public interface TweetService {
    /**
     * To get all tweets
     *
     * @return TweetResponse
     * @param token
     */
    ResponseEntity<TweetResponse> getAllTweets(String token);

    /**
     * To get all tweet based on Username
     *
     *
     * @param token
     * @param userName
     * @return TweetResponse
     */
    ResponseEntity<TweetResponse> getAllTweetsOfUser(String token, String userName);

    /**
     * To update Tweet
     *
     *
     * @param token
     * @param userName
     * @param id
     * @param tweet
     * @return TweetResponse
     */
    ResponseEntity<TweetResponse> updateTweet(String token, String userName, Integer id, Tweet tweet);

    /**
     * To Post new tweet
     *
     *
     * @param token
     * @param userName
     * @param tweet
     * @return TweetResponse
     */
    ResponseEntity<TweetResponse> postNewTweet(String token, String userName, Tweet tweet);

    /**
     * To delete Tweet
     *
     *
     * @param token
     * @param userName
     * @param id
     * @return TweetResponse
     */
    ResponseEntity<TweetResponse> deleteTweet(String token, String userName, Integer id);

    /**
     * To Like/Unlike a Tweet
     *
     *
     * @param token
     * @param userName
     * @param id
     * @return TweetResponse
     */
    ResponseEntity<TweetResponse> likeTweet(String token, String userName, Integer id);

    /**
     * To reply to a Tweet
     *
     *
     * @param token
     * @param userName
     * @param id
     * @param tweet
     * @return TweetResponse
     */
    ResponseEntity<TweetResponse> replyToTweet(String token, String userName, Integer id, Tweet tweet);
}
