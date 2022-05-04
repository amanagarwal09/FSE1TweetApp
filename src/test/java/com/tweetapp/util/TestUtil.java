package com.tweetapp.util;

import com.tweetapp.entity.TweetEntity;
import com.tweetapp.entity.TweetLikeEntity;
import com.tweetapp.entity.UserEntity;
import com.tweetapp.model.Tweet;
import com.tweetapp.model.User;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

public class TestUtil {

    public static List<TweetEntity> sampleTweetEntityList() {
        return Arrays.asList(TweetEntity.builder()
                .tweetId(31)
                .createdDate(LocalDateTime.now())
                .parentTweetId(20)
                .tweetDesc("My first tweet")
                .userId(7)
                .build(), TweetEntity.builder()
                .tweetId(32)
                .createdDate(LocalDateTime.now())
                .parentTweetId(22)
                .tweetDesc("My second tweet")
                .userId(7)
                .build());
    }

    public static List<TweetLikeEntity> sampleTweetLikeEntityList() {
        return Arrays.asList(TweetLikeEntity.builder()
                .tweetId(21)
                .tweetLikeId(1)
                .userId(1)
                .build(), TweetLikeEntity.builder()
                .tweetId(22)
                .tweetLikeId(2)
                .userId(1)
                .build());
    }

    public static UserEntity sampleUserEntity() {
        UserEntity userEntity = new UserEntity();
        userEntity.setUserId(1);
        userEntity.setPassword("dontTellAnyone");
        userEntity.setContactNumber(2030405060);
        userEntity.setFirstName("Akash");
        userEntity.setLastName("Soni");
        userEntity.setLoginId("TestUser007");
        userEntity.setEmail("testemail@mail.com");
        return userEntity;
    }

    public static Tweet sampleTweet() {
        return Tweet.builder()
                .tweetId(41)
                .parentTweetId(21)
                .likeCount(10)
                .tweetDesc("Visiting Betul ;-)")
                .createdDate(LocalDateTime.now())
                .userId(7)
                .build();
    }

    public static User sampleUser() {
        return User.builder()
                .confirmPassword("test")
                .password("test")
                .userId(1)
                .contactNumber(123567l)
                .email("test@gmail.com")
                .loginId("test")
                .firstName("test")
                .lastName("test")
                .build();
    }
}
