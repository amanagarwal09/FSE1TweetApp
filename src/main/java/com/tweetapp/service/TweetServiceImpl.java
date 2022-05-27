package com.tweetapp.service;

import com.tweetapp.entity.TweetEntity;
import com.tweetapp.entity.TweetLikeEntity;
import com.tweetapp.entity.UserEntity;
import com.tweetapp.model.Tweet;
import com.tweetapp.model.TweetResponse;
import com.tweetapp.model.UserResponse;
import com.tweetapp.producer.TweetProducer;
import com.tweetapp.repository.TweetLikeRepository;
import com.tweetapp.repository.TweetRepository;
import com.tweetapp.repository.UserRepository;
import com.tweetapp.utils.EntityModelMapper;
import com.tweetapp.utils.ServiceConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class TweetServiceImpl implements TweetService {

    @Autowired
    private TweetRepository tweetRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TweetLikeRepository tweetLikeRepository;
    @Autowired
    private SequenceService sequenceService;
    @Autowired
    private TweetProducer tweetProducer;
    @Autowired
    private UserService userService;

    /**
     * To get all tweets
     *
     * @param token
     * @return TweetResponse
     */
    @Override
    public ResponseEntity<TweetResponse> getAllTweets(String token) {
        try {
            if (userService.validateToken(token)) {
                List<TweetEntity> tweetEntityList = tweetRepository.findAll();
                if (tweetEntityList.isEmpty()) {
                    return new ResponseEntity<>(TweetResponse.builder().message(ServiceConstants.NO_TWEET)
                            .messageCode(HttpStatus.NOT_FOUND)
                            .messageType(ServiceConstants.FAILURE)
                            .build(), HttpStatus.NOT_FOUND);
                }
                List<Tweet> tweetList = new ArrayList<>();
                for (TweetEntity tweetEntity : tweetEntityList) {
                    Tweet tweet = EntityModelMapper.tweetEntityToTweet(tweetEntity);
                    List<TweetLikeEntity> tweetLikeEntityList = tweetLikeRepository.findByTweetId(tweet.getTweetId());
                    tweet.setLikeCount(tweetLikeEntityList.size());
                    tweetList.add(tweet);
                }
                return new ResponseEntity<>(TweetResponse.builder().message(ServiceConstants.SUCCESS)
                        .tweetList(tweetList)
                        .messageCode(HttpStatus.OK)
                        .messageType(ServiceConstants.SUCCESS)
                        .build(), HttpStatus.OK);
            } else {
                new ResponseEntity<>(TweetResponse.builder().message(ServiceConstants.FAILURE)
                        .messageCode(HttpStatus.FORBIDDEN)
                        .messageType(ServiceConstants.FAILURE)
                        .build(), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (Exception e) {
            log.error("Error while Getting all tweets {}", e.getMessage());
        }
        return new ResponseEntity<>(TweetResponse.builder().message(ServiceConstants.FAILURE)
                .messageCode(HttpStatus.INTERNAL_SERVER_ERROR)
                .messageType(ServiceConstants.FAILURE)
                .build(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * To get all tweet based on Username
     *
     * @param token
     * @param userName
     * @return TweetResponse
     */
    @Override
    public ResponseEntity<TweetResponse> getAllTweetsOfUser(String token, String userName) {
        try {
            if (userService.validateToken(token)) {
                Optional<UserEntity> optionalUserLoginCheck = userRepository.findByLoginId(userName);
                if (optionalUserLoginCheck.isPresent()) {
                    List<TweetEntity> tweetEntityList = tweetRepository.findByUserId(optionalUserLoginCheck.get().getUserId());
                    if (tweetEntityList.isEmpty()) {
                        return new ResponseEntity<>(TweetResponse.builder().message(ServiceConstants.NO_TWEET)
                                .messageCode(HttpStatus.NOT_FOUND)
                                .messageType(ServiceConstants.FAILURE)
                                .build(), HttpStatus.NOT_FOUND);
                    }
                    List<Tweet> tweetList = new ArrayList<>();
                    for (TweetEntity tweetEntity : tweetEntityList) {
                        Tweet tweet = EntityModelMapper.tweetEntityToTweet(tweetEntity);
                        List<TweetLikeEntity> tweetLikeEntityList = tweetLikeRepository.findByTweetId(tweet.getTweetId());
                        tweet.setLikeCount(tweetLikeEntityList.size());
                        tweetList.add(tweet);
                    }
                    return new ResponseEntity<>(TweetResponse.builder().message(ServiceConstants.SUCCESS)
                            .tweetList(tweetList)
                            .messageCode(HttpStatus.OK)
                            .messageType(ServiceConstants.SUCCESS)
                            .build(), HttpStatus.OK);
                }
                return new ResponseEntity<>(TweetResponse.builder().message(ServiceConstants.USER_NOT_EXIST)
                        .messageCode(HttpStatus.INTERNAL_SERVER_ERROR)
                        .messageType(ServiceConstants.FAILURE)
                        .build(), HttpStatus.INTERNAL_SERVER_ERROR);
            } else {
                new ResponseEntity<>(TweetResponse.builder().message(ServiceConstants.FAILURE)
                        .messageCode(HttpStatus.FORBIDDEN)
                        .messageType(ServiceConstants.FAILURE)
                        .build(), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (Exception e) {
            log.error("Error while Getting all tweets Of User{}", e.getMessage());
        }
        return new ResponseEntity<>(TweetResponse.builder().message(ServiceConstants.FAILURE)
                .messageCode(HttpStatus.INTERNAL_SERVER_ERROR)
                .messageType(ServiceConstants.FAILURE)
                .build(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * To update Tweet
     *
     * @param token
     * @param userName
     * @param id
     * @param tweet
     * @return TweetResponse
     */
    @Override
    public ResponseEntity<TweetResponse> updateTweet(String token, String userName, Integer id, Tweet tweet) {
        try {
            if (userService.validateToken(token)) {
                Optional<UserEntity> optionalUserLoginCheck = userRepository.findByLoginId(userName);
                if (optionalUserLoginCheck.isPresent()) {
                    Optional<TweetEntity> optionalTweetEntity = tweetRepository.findById(id);
                    if (optionalTweetEntity.isPresent()) {
                        tweet.setTweetId(id);
                        tweet.setCreatedDate(optionalTweetEntity.get().getCreatedDate());
                        tweet.setTweetDesc(tweet.getTweetDesc());
                        tweet.setParentTweetId(optionalTweetEntity.get().getParentTweetId());
                        tweet.setUserId(optionalUserLoginCheck.get().getUserId());

                        TweetEntity tweetEntity = EntityModelMapper.tweetToTweetEntity(tweet);
                        tweetRepository.save(tweetEntity);
                        return new ResponseEntity<>(TweetResponse.builder().message(ServiceConstants.SUCCESS)
                                .messageCode(HttpStatus.OK)
                                .messageType(ServiceConstants.SUCCESS)
                                .build(), HttpStatus.OK);
                    }
                    return new ResponseEntity<>(TweetResponse.builder().message(ServiceConstants.NO_TWEET)
                            .messageCode(HttpStatus.NOT_FOUND)
                            .messageType(ServiceConstants.FAILURE)
                            .build(), HttpStatus.NOT_FOUND);
                }
                return new ResponseEntity<>(TweetResponse.builder().message(ServiceConstants.USER_NOT_EXIST)
                        .messageCode(HttpStatus.INTERNAL_SERVER_ERROR)
                        .messageType(ServiceConstants.FAILURE)
                        .build(), HttpStatus.INTERNAL_SERVER_ERROR);
            } else {
                new ResponseEntity<>(TweetResponse.builder().message(ServiceConstants.FAILURE)
                        .messageCode(HttpStatus.FORBIDDEN)
                        .messageType(ServiceConstants.FAILURE)
                        .build(), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (Exception e) {
            log.error("Error while Getting update tweet{}", e.getMessage());
        }
        return new ResponseEntity<>(TweetResponse.builder()
                .message(ServiceConstants.FAILURE)
                .messageCode(HttpStatus.INTERNAL_SERVER_ERROR)
                .messageType(ServiceConstants.FAILURE)
                .build(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * To Post new tweet
     *
     * @param token
     * @param userName
     * @param tweet
     * @return TweetResponse
     */
    @Override
    public ResponseEntity<TweetResponse> postNewTweet(String token, String userName, Tweet tweet) {
        try {
            if (userService.validateToken(token)) {
                Optional<UserEntity> optionalUserLoginCheck = userRepository.findByLoginId(userName);
                if (optionalUserLoginCheck.isPresent()) {
                    tweet.setUserId(optionalUserLoginCheck.get().getUserId());
                    TweetEntity tweetEntity = EntityModelMapper.tweetToTweetEntity(tweet);
                    tweetEntity.setTweetId(sequenceService.getNextSequence(TweetEntity.SEQUENCE_NAME));
                    tweetProducer.sendMessage(tweetEntity);

                    return new ResponseEntity<>(TweetResponse.builder().message(ServiceConstants.SUCCESS)
                            .messageCode(HttpStatus.OK)
                            .messageType(ServiceConstants.SUCCESS)
                            .build(), HttpStatus.OK);
                }
                return new ResponseEntity<>(TweetResponse.builder().message(ServiceConstants.USER_NOT_EXIST)
                        .messageCode(HttpStatus.INTERNAL_SERVER_ERROR)
                        .messageType(ServiceConstants.FAILURE)
                        .build(), HttpStatus.INTERNAL_SERVER_ERROR);
            } else {
                new ResponseEntity<>(TweetResponse.builder().message(ServiceConstants.FAILURE)
                        .messageCode(HttpStatus.FORBIDDEN)
                        .messageType(ServiceConstants.FAILURE)
                        .build(), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (Exception e) {
            log.error("Error while post New Tweet{}", e.getMessage());
        }
        return new ResponseEntity<>(TweetResponse.builder()
                .message(ServiceConstants.FAILURE)
                .messageCode(HttpStatus.INTERNAL_SERVER_ERROR)
                .messageType(ServiceConstants.FAILURE)
                .build(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * To delete Tweet
     *
     * @param token
     * @param userName
     * @param id
     * @return TweetResponse
     */
    @Override
    public ResponseEntity<TweetResponse> deleteTweet(String token, String userName, Integer id) {
        try {
            if (userService.validateToken(token)) {
                Optional<UserEntity> optionalUserLoginCheck = userRepository.findByLoginId(userName);
                if (optionalUserLoginCheck.isPresent()) {
                    Optional<TweetEntity> optionalTweetEntity = tweetRepository.findById(id);
                    if (optionalTweetEntity.isPresent()) {
                        tweetRepository.deleteById(id);
                        return new ResponseEntity<>(TweetResponse.builder().message(ServiceConstants.SUCCESS)
                                .messageCode(HttpStatus.OK)
                                .messageType(ServiceConstants.SUCCESS)
                                .build(), HttpStatus.OK);
                    }
                    return new ResponseEntity<>(TweetResponse.builder().message(ServiceConstants.NO_TWEET)
                            .messageCode(HttpStatus.NOT_FOUND)
                            .messageType(ServiceConstants.FAILURE)
                            .build(), HttpStatus.NOT_FOUND);
                }
                return new ResponseEntity<>(TweetResponse.builder().message(ServiceConstants.USER_NOT_EXIST)
                        .messageCode(HttpStatus.INTERNAL_SERVER_ERROR)
                        .messageType(ServiceConstants.FAILURE)
                        .build(), HttpStatus.INTERNAL_SERVER_ERROR);
            } else {
                new ResponseEntity<>(TweetResponse.builder().message(ServiceConstants.FAILURE)
                        .messageCode(HttpStatus.FORBIDDEN)
                        .messageType(ServiceConstants.FAILURE)
                        .build(), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (Exception e) {
            log.error("Error while Deleting Tweet{}", e.getMessage());
        }
        return new ResponseEntity<>(TweetResponse.builder()
                .message(ServiceConstants.FAILURE)
                .messageCode(HttpStatus.INTERNAL_SERVER_ERROR)
                .messageType(ServiceConstants.FAILURE)
                .build(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * To Like/Unlike a Tweet
     *
     * @param token
     * @param userName
     * @param id
     * @return TweetResponse
     */
    @Override
    public ResponseEntity<TweetResponse> likeTweet(String token, String userName, Integer id) {
        try {
            if (userService.validateToken(token)) {
                Optional<UserEntity> optionalUserLoginCheck = userRepository.findByLoginId(userName);
                if (optionalUserLoginCheck.isPresent()) {
                    Optional<TweetEntity> optionalTweetEntity = tweetRepository.findById(id);
                    if (optionalTweetEntity.isPresent()) {
                        Optional<TweetLikeEntity> optionalTweetLikeEntity = tweetLikeRepository.findByUserIdAndTweetId
                                (optionalUserLoginCheck.get().getUserId(), id);
                        if (optionalTweetLikeEntity.isPresent()) {
                            tweetLikeRepository.deleteById(optionalTweetLikeEntity.get().getTweetLikeId());
                            return new ResponseEntity<>(TweetResponse.builder().message(ServiceConstants.UNLIKE_TWEET)
                                    .messageCode(HttpStatus.OK)
                                    .messageType(ServiceConstants.SUCCESS)
                                    .build(), HttpStatus.OK);
                        }
                        tweetLikeRepository.save(TweetLikeEntity.builder()
                                .tweetLikeId(sequenceService.getNextSequence(TweetLikeEntity.SEQUENCE_NAME))
                                //.tweetEntity(optionalTweetEntity.get())
                                .tweetId(optionalTweetEntity.get().getTweetId())
                                .userId(optionalUserLoginCheck.get().getUserId())
                                .build());
                        return new ResponseEntity<>(TweetResponse.builder().message(ServiceConstants.LIKE_TWEET)
                                .messageCode(HttpStatus.OK)
                                .messageType(ServiceConstants.SUCCESS)
                                .build(), HttpStatus.OK);
                    }
                    return new ResponseEntity<>(TweetResponse.builder().message(ServiceConstants.NO_TWEET)
                            .messageCode(HttpStatus.NOT_FOUND)
                            .messageType(ServiceConstants.FAILURE)
                            .build(), HttpStatus.NOT_FOUND);
                }
                return new ResponseEntity<>(TweetResponse.builder().message(ServiceConstants.USER_NOT_EXIST)
                        .messageCode(HttpStatus.INTERNAL_SERVER_ERROR)
                        .messageType(ServiceConstants.FAILURE)
                        .build(), HttpStatus.INTERNAL_SERVER_ERROR);
            } else {
                new ResponseEntity<>(TweetResponse.builder().message(ServiceConstants.FAILURE)
                        .messageCode(HttpStatus.FORBIDDEN)
                        .messageType(ServiceConstants.FAILURE)
                        .build(), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (Exception e) {
            log.error("Error while Liking Tweet{}", e.getMessage());
        }
        return new ResponseEntity<>(TweetResponse.builder()
                .message(ServiceConstants.FAILURE)
                .messageCode(HttpStatus.INTERNAL_SERVER_ERROR)
                .messageType(ServiceConstants.FAILURE)
                .build(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * To reply to a Tweet
     *
     * @param token
     * @param userName
     * @param id
     * @param tweet
     * @return TweetResponse
     */
    @Override
    public ResponseEntity<TweetResponse> replyToTweet(String token, String userName, Integer id, Tweet tweet) {
        try {
            if (userService.validateToken(token)) {
                Optional<TweetEntity> optionalTweetEntity = tweetRepository.findById(id);
                if (optionalTweetEntity.isPresent()) {
                    tweet.setParentTweetId(id);
                    return postNewTweet(token, userName, tweet);
                }
                return new ResponseEntity<>(TweetResponse.builder().message(ServiceConstants.NO_TWEET)
                        .messageCode(HttpStatus.NOT_FOUND)
                        .messageType(ServiceConstants.FAILURE)
                        .build(), HttpStatus.NOT_FOUND);
            } else {
                new ResponseEntity<>(TweetResponse.builder().message(ServiceConstants.FAILURE)
                        .messageCode(HttpStatus.FORBIDDEN)
                        .messageType(ServiceConstants.FAILURE)
                        .build(), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (Exception e) {
            log.error("Error while Replying to Tweet{}", e.getMessage());
        }
        return new ResponseEntity<>(TweetResponse.builder()
                .message(ServiceConstants.FAILURE)
                .messageCode(HttpStatus.INTERNAL_SERVER_ERROR)
                .messageType(ServiceConstants.FAILURE)
                .build(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
