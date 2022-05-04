package com.tweetapp.utils;

import com.tweetapp.entity.TweetEntity;
import com.tweetapp.entity.UserEntity;
import com.tweetapp.model.Tweet;
import com.tweetapp.model.User;

public class EntityModelMapper {

    private EntityModelMapper() {

    }

    /**
     * To map Tweet class to TweetEntity class
     *
     * @param tweet
     * @return TweetEntity
     */
    public static TweetEntity tweetToTweetEntity(Tweet tweet) {
        return TweetEntity.builder()
                .tweetId(tweet.getTweetId())
                .createdDate(tweet.getCreatedDate())
                .tweetDesc(tweet.getTweetDesc())
                .userId(tweet.getUserId())
                .parentTweetId(tweet.getParentTweetId())
                .build();
    }

    /**
     * To map TweetEntity class to Tweet class
     *
     * @param tweetEntity
     * @return Tweet
     */
    public static Tweet tweetEntityToTweet(TweetEntity tweetEntity) {
        return Tweet.builder()
                .tweetId(tweetEntity.getTweetId())
                .createdDate(tweetEntity.getCreatedDate())
                .tweetDesc(tweetEntity.getTweetDesc())
                .userId(tweetEntity.getUserId())
                .parentTweetId(tweetEntity.getParentTweetId())
                .build();
    }

    /**
     * To map User class to UserEntity class
     *
     * @param user
     * @return UserEntity
     */
    public static UserEntity userToUserEntity(User user) {
        return UserEntity.builder()
                .userId(user.getUserId())
                .contactNumber(user.getContactNumber())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .loginId(user.getLoginId())
                .password(user.getPassword())
                .build();
    }

    /**
     * To map UserEntity class to User class
     *
     * @param userEntity
     * @return User
     */
    public static User userEntityToUser(UserEntity userEntity) {
        return User.builder()
                .userId(userEntity.getUserId())
                .contactNumber(userEntity.getContactNumber())
                .email(userEntity.getEmail())
                .firstName(userEntity.getFirstName())
                .lastName(userEntity.getLastName())
                .loginId(userEntity.getLoginId())
                .password(userEntity.getPassword())
                .build();
    }

}
