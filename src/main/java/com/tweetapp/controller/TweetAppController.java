package com.tweetapp.controller;

import com.tweetapp.model.*;
import com.tweetapp.service.TweetService;
import com.tweetapp.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1.0/tweets")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Slf4j
public class TweetAppController {

    @Autowired
    private TweetService tweetService;
    @Autowired
    private UserService userService;

    /**
     * API to register user
     *
     * @param user
     * @return UserResponse
     */
    @PostMapping("/register")
    public ResponseEntity<UserResponse> registerUser(@RequestBody @Valid User user) {
        log.info("Register User");
        return userService.registerUser(user);
    }

    /**
     * API for user to login
     *
     * @param loginId
     * @param password
     * @return UserResponse
     */
    @PostMapping("/login")
    public ResponseEntity<UserResponse> login(@RequestParam String loginId,
                                              @RequestParam String password) {
        return userService.login(loginId, password);
    }

    /**
     * API for forgot password
     *
     * @param userName
     * @return UserResponse
     */
    @GetMapping("/{username}/forgot")
    public ResponseEntity<UserResponse> forgotPassword(@PathVariable("username") String userName) {
        return userService.forgotPassword(userName);
    }

    /**
     * API to reset Password
     *
     * @param userName
     * @param user
     * @return UserResponse
     */
    @PostMapping("/{username}/resetpassword")
    public ResponseEntity<UserResponse> resetPassword(@PathVariable("username") String userName, @RequestBody User user) {
        return userService.resetPassword(userName, user);
    }

    /**
     * API to get all tweets
     *
     * @return TweetResponse
     */
    @GetMapping("/all")
    public ResponseEntity<TweetResponse> getAllTweets(@RequestHeader("Authorization") final String token) {
        return tweetService.getAllTweets(token);
    }

    /**
     * API to get all users
     *
     * @return UserResponse
     */
    @GetMapping("/users/all")
    public ResponseEntity<UserResponse> getAllUsers(@RequestHeader("Authorization") final String token) {
        return userService.getAllUsers(token);
    }

    /**
     * API to search user based on Username
     *
     * @param userName
     * @return UserResponse
     */
    @GetMapping("/user/search/{username}")
    public ResponseEntity<UserResponse> searchByUserName(@RequestHeader("Authorization") final String token,@PathVariable("username") String userName) {
        return userService.searchByUserName(token, userName);
    }

    /**
     * API to search user based on Username
     *
     * @param userName
     * @return UserResponse
     */
    @GetMapping("/user/{username}")
    public ResponseEntity<UserResponse> getByUserName(@RequestHeader("Authorization") final String token,@PathVariable("username") String userName) {
        return userService.getByUserName(token, userName);
    }

    /**
     * API to get all tweet based on Username
     *
     * @param userName
     * @return TweetResponse
     */
    @GetMapping("/{username}")
    public ResponseEntity<TweetResponse> getAllTweetsOfUser(@RequestHeader("Authorization") final String token,@PathVariable("username") String userName) {
        return tweetService.getAllTweetsOfUser(token, userName);
    }

    /**
     * API to Post new tweet
     *
     * @param userName
     * @param tweet
     * @return TweetResponse
     */
    @PostMapping("/{username}/add")
    public ResponseEntity<TweetResponse> postNewTweet(@RequestHeader("Authorization") final String token,@PathVariable("username") String userName, @RequestBody @Valid Tweet tweet) {
        return tweetService.postNewTweet(token, userName, tweet);
    }

    /**
     * API to update Tweet
     *
     * @param userName
     * @param id
     * @param tweet
     * @return TweetResponse
     */
    @PutMapping("/{username}/update/{id}")
    public ResponseEntity<TweetResponse> updateTweet(@RequestHeader("Authorization") final String token,@PathVariable("username") String userName, @PathVariable("id") Integer id, @RequestBody Tweet tweet) {
        return tweetService.updateTweet(token, userName, id, tweet);
    }

    /**
     * API to delete Tweet
     *
     * @param userName
     * @param id
     * @return TweetResponse
     */
    @DeleteMapping("/{username}/delete/{id}")
    public ResponseEntity<TweetResponse> deleteTweet(@RequestHeader("Authorization") final String token,@PathVariable("username") String userName, @PathVariable("id") Integer id) {
        return tweetService.deleteTweet(token, userName, id);
    }

    /**
     * API to Like/Unlike a Tweet
     *
     * @param userName
     * @param id
     * @return TweetResponse
     */
    @PutMapping("/{username}/like/{id}")
    public ResponseEntity<TweetResponse> likeTweet(@RequestHeader("Authorization") final String token,@PathVariable("username") String userName, @PathVariable("id") Integer id) {
        return tweetService.likeTweet(token, userName, id);
    }

    /**
     * API to reply to a Tweet
     *
     * @param userName
     * @param id
     * @param tweet
     * @return TweetResponse
     */
    @PostMapping("/{username}/reply/{id}")
    public ResponseEntity<TweetResponse> replyToTweet(@RequestHeader("Authorization") final String token, @PathVariable("username") String userName, @PathVariable("id") Integer id, @RequestBody @Valid Tweet tweet) {
        return tweetService.replyToTweet(token, userName, id, tweet);
    }
}
