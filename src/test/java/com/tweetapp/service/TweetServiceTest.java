package com.tweetapp.service;

import com.tweetapp.model.TweetResponse;
import com.tweetapp.producer.TweetProducer;
import com.tweetapp.repository.TweetLikeRepository;
import com.tweetapp.repository.TweetRepository;
import com.tweetapp.repository.UserRepository;
import com.tweetapp.util.TestUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TweetServiceTest {

    @InjectMocks
    TweetServiceImpl tweetService;

    @Mock
    TweetRepository tweetRepository;

    @Mock
    UserRepository userRepository;

    @Mock
    TweetLikeRepository tweetLikeRepository;

    @Mock
    SequenceService sequenceService;

    @Mock
    TweetProducer tweetProducer;

    @Mock
    UserService userService;

    @Test
    void testGetAllTweets() {
        when(userService.validateToken(Mockito.anyString())).thenReturn(true);
        when(tweetRepository.findAll()).thenReturn(TestUtil.sampleTweetEntityList());
        when(tweetLikeRepository.findByTweetId(Mockito.anyInt())).thenReturn(TestUtil.sampleTweetLikeEntityList());
        ResponseEntity<TweetResponse> response = tweetService.getAllTweets("token");
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testGetAllTweetsEmptyTweet() {
        when(userService.validateToken(Mockito.anyString())).thenReturn(true);
        when(tweetRepository.findAll()).thenReturn(Collections.emptyList());
        ResponseEntity<TweetResponse> response = tweetService.getAllTweets("token");
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testGetAllTweetsException() {
        when(userService.validateToken(Mockito.anyString())).thenReturn(true);
        when(tweetRepository.findAll()).thenThrow(NullPointerException.class);
        ResponseEntity<TweetResponse> response = tweetService.getAllTweets("token");
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    void testGetAllTweetsOfUser() {
        when(userService.validateToken(Mockito.anyString())).thenReturn(true);
        when(userRepository.findByLoginId(Mockito.anyString()))
                .thenReturn(Optional.of(TestUtil.sampleUserEntity()));
        when(tweetRepository.findByUserId(Mockito.anyInt()))
                .thenReturn(TestUtil.sampleTweetEntityList());
        when(tweetLikeRepository.findByTweetId(Mockito.anyInt()))
                .thenReturn(TestUtil.sampleTweetLikeEntityList());
        ResponseEntity<TweetResponse> response = tweetService.getAllTweetsOfUser("token", "Akash");
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testGetAllTweetsOfUserNotFound() {
        when(userService.validateToken(Mockito.anyString())).thenReturn(true);
        when(userRepository.findByLoginId(Mockito.anyString()))
                .thenReturn(Optional.empty());
        ResponseEntity<TweetResponse> response = tweetService.getAllTweetsOfUser("token", "Akash");
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    void testGetAllTweetsOfUserEmptyTweetList() {
        when(userService.validateToken(Mockito.anyString())).thenReturn(true);
        when(userRepository.findByLoginId(Mockito.anyString()))
                .thenReturn(Optional.of(TestUtil.sampleUserEntity()));
        when(tweetRepository.findByUserId(Mockito.anyInt()))
                .thenReturn(Collections.emptyList());
        ResponseEntity<TweetResponse> response = tweetService.getAllTweetsOfUser("token", "Akash");
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testGetAllTweetsOfUserException() {
        when(userService.validateToken(Mockito.anyString())).thenReturn(true);
        when(userRepository.findByLoginId(Mockito.anyString()))
                .thenThrow(NullPointerException.class);
        ResponseEntity<TweetResponse> response = tweetService.getAllTweetsOfUser("token", "Akash");
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    void testUpdateTweet() {
        when(userService.validateToken(Mockito.anyString())).thenReturn(true);
        when(userRepository.findByLoginId(Mockito.anyString()))
                .thenReturn(Optional.of(TestUtil.sampleUserEntity()));
        when(tweetRepository.findById(Mockito.anyInt()))
                .thenReturn(Optional.of(TestUtil.sampleTweetEntityList().get(0)));
        ResponseEntity<TweetResponse> response = tweetService.updateTweet("token", "Aman", 7, TestUtil.sampleTweet());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testUpdateTweetEmptyTweetList() {
        when(userService.validateToken(Mockito.anyString())).thenReturn(true);
        when(userRepository.findByLoginId(Mockito.anyString()))
                .thenReturn(Optional.of(TestUtil.sampleUserEntity()));
        when(tweetRepository.findById(Mockito.anyInt()))
                .thenReturn(Optional.empty());
        ResponseEntity<TweetResponse> response = tweetService.updateTweet("token", "Aman", 7, TestUtil.sampleTweet());
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testUpdateTweetUserNotFound() {
        when(userService.validateToken(Mockito.anyString())).thenReturn(true);
        when(userRepository.findByLoginId(Mockito.anyString()))
                .thenReturn(Optional.empty());
        ResponseEntity<TweetResponse> response = tweetService.updateTweet("token", "Aman", 7, TestUtil.sampleTweet());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    void testUpdateTweetException() {
        when(userService.validateToken(Mockito.anyString())).thenReturn(true);
        when(userRepository.findByLoginId(Mockito.anyString()))
                .thenThrow(NullPointerException.class);
        ResponseEntity<TweetResponse> response = tweetService.updateTweet("token", "Akash", 7, TestUtil.sampleTweet());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    void testPostNewTweet() {
        when(userService.validateToken(Mockito.anyString())).thenReturn(true);
        when(userRepository.findByLoginId(Mockito.anyString()))
                .thenReturn(Optional.of(TestUtil.sampleUserEntity()));
        when(sequenceService.getNextSequence(Mockito.anyString()))
                .thenReturn(408);
        ResponseEntity<TweetResponse> response = tweetService.postNewTweet("token", "Akash", TestUtil.sampleTweet());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testPostNewTweetUserNotFound() {
        when(userService.validateToken(Mockito.anyString())).thenReturn(true);
        when(userRepository.findByLoginId(Mockito.anyString()))
                .thenReturn(Optional.empty());
        ResponseEntity<TweetResponse> response = tweetService.postNewTweet("token", "Akash", TestUtil.sampleTweet());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    void testPostNewTweetException() {
        when(userService.validateToken(Mockito.anyString())).thenReturn(true);
        when(userRepository.findByLoginId(Mockito.anyString()))
                .thenThrow(NullPointerException.class);
        ResponseEntity<TweetResponse> response = tweetService.postNewTweet("token", "Akash", TestUtil.sampleTweet());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    void testDeleteTweet() {
        when(userService.validateToken(Mockito.anyString())).thenReturn(true);
        when(userRepository.findByLoginId(Mockito.anyString()))
                .thenReturn(Optional.of(TestUtil.sampleUserEntity()));
        when(tweetRepository.findById(Mockito.anyInt()))
                .thenReturn(Optional.of(TestUtil.sampleTweetEntityList().get(0)));
        ResponseEntity<TweetResponse> response = tweetService.deleteTweet("token", "Akash", 12);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testDeleteTweetEmptyTweet() {
        when(userService.validateToken(Mockito.anyString())).thenReturn(true);
        when(userRepository.findByLoginId(Mockito.anyString()))
                .thenReturn(Optional.of(TestUtil.sampleUserEntity()));
        when(tweetRepository.findById(Mockito.anyInt()))
                .thenReturn(Optional.empty());
        ResponseEntity<TweetResponse> response = tweetService.deleteTweet("token", "Akash", 12);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testDeleteTweetUserNotFound() {
        when(userService.validateToken(Mockito.anyString())).thenReturn(true);
        when(userRepository.findByLoginId(Mockito.anyString()))
                .thenReturn(Optional.empty());
        ResponseEntity<TweetResponse> response = tweetService.deleteTweet("token", "Akash", 12);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    void testDeleteTweetException() {
        when(userService.validateToken(Mockito.anyString())).thenReturn(true);
        when(userRepository.findByLoginId(Mockito.anyString()))
                .thenThrow(NullPointerException.class);
        ResponseEntity<TweetResponse> response = tweetService.deleteTweet("token", "Akash", 12);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    void testLikeTweet() {
        when(userService.validateToken(Mockito.anyString())).thenReturn(true);
        when(userRepository.findByLoginId(Mockito.anyString()))
                .thenReturn(Optional.of(TestUtil.sampleUserEntity()));
        when(tweetRepository.findById(Mockito.anyInt()))
                .thenReturn(Optional.of(TestUtil.sampleTweetEntityList().get(0)));
        when(tweetLikeRepository.findByUserIdAndTweetId(Mockito.anyInt(), Mockito.anyInt()))
                .thenReturn(Optional.of(TestUtil.sampleTweetLikeEntityList().get(0)));
        ResponseEntity<TweetResponse> response = tweetService.likeTweet("token", "Aman", 7);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testLikeTweet2() {
        when(userService.validateToken(Mockito.anyString())).thenReturn(true);
        when(userRepository.findByLoginId(Mockito.anyString()))
                .thenReturn(Optional.of(TestUtil.sampleUserEntity()));
        when(tweetRepository.findById(Mockito.anyInt()))
                .thenReturn(Optional.of(TestUtil.sampleTweetEntityList().get(0)));
        when(tweetLikeRepository.findByUserIdAndTweetId(Mockito.anyInt(), Mockito.anyInt()))
                .thenReturn(Optional.empty());
        ResponseEntity<TweetResponse> response = tweetService.likeTweet("token", "Aman", 7);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testLikeTweetEmptyTweetList() {
        when(userService.validateToken(Mockito.anyString())).thenReturn(true);
        when(userRepository.findByLoginId(Mockito.anyString()))
                .thenReturn(Optional.of(TestUtil.sampleUserEntity()));
        when(tweetRepository.findById(Mockito.anyInt()))
                .thenReturn(Optional.empty());
        ResponseEntity<TweetResponse> response = tweetService.likeTweet("token", "Aman", 7);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testLikeTweetUserNotExist() {
        when(userService.validateToken(Mockito.anyString())).thenReturn(true);
        when(userRepository.findByLoginId(Mockito.anyString()))
                .thenReturn(Optional.empty());
        ResponseEntity<TweetResponse> response = tweetService.likeTweet("token", "Aman", 7);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    void testLikeTweetException() {
        when(userService.validateToken(Mockito.anyString())).thenReturn(true);
        when(userRepository.findByLoginId(Mockito.anyString()))
                .thenThrow(NullPointerException.class);
        ResponseEntity<TweetResponse> response = tweetService.likeTweet("token", "Aman", 7);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    void testReplyToTweet() {
        when(userService.validateToken(Mockito.anyString())).thenReturn(true);
        when(tweetRepository.findById(Mockito.anyInt()))
                .thenReturn(Optional.of(TestUtil.sampleTweetEntityList().get(0)));
        when(userRepository.findByLoginId(Mockito.anyString()))
                .thenReturn(Optional.of(TestUtil.sampleUserEntity()));
        when(sequenceService.getNextSequence(Mockito.anyString()))
                .thenReturn(708);
        ResponseEntity<TweetResponse> response = tweetService.replyToTweet("token", "Akash", 1, TestUtil.sampleTweet());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testReplyToTweetNotFound() {
        when(userService.validateToken(Mockito.anyString())).thenReturn(true);
        when(tweetRepository.findById(Mockito.anyInt()))
                .thenReturn(Optional.empty());
        ResponseEntity<TweetResponse> response = tweetService.replyToTweet("token", "Akash", 1, TestUtil.sampleTweet());
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testReplyToTweetException() {
        when(userService.validateToken(Mockito.anyString())).thenReturn(true);
        when(tweetRepository.findById(Mockito.anyInt()))
                .thenThrow(NullPointerException.class);
        ResponseEntity<TweetResponse> response = tweetService.replyToTweet("token", "Akash", 1, TestUtil.sampleTweet());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }
}
