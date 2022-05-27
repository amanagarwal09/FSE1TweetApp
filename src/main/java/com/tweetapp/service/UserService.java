package com.tweetapp.service;

import com.tweetapp.model.User;
import com.tweetapp.model.UserResponse;
import org.springframework.http.ResponseEntity;

public interface UserService {

    /**
     * To register user
     *
     * @param user
     * @return UserResponse
     */
    ResponseEntity<UserResponse> registerUser(User user);

    /**
     * For forgot password
     *
     * @param userName
     * @return UserResponse
     */
    ResponseEntity<UserResponse> forgotPassword(String userName);

    /**
     * To get all tweets
     *
     * @param token
     * @return TweetResponse
     */
    ResponseEntity<UserResponse> getAllUsers(String token);

    /**
     * To search user based on Username
     *
     * @param token
     * @param userName
     * @return UserResponse
     */
    ResponseEntity<UserResponse> searchByUserName(String token, String userName);

    /**
     * To reset Password
     *
     * @param userName
     * @param user
     * @return UserResponse
     */
    ResponseEntity<UserResponse> resetPassword(String userName, User user);

    /**
     * To user to login
     *
     * @param loginId
     * @param password
     * @return UserResponse
     */
    ResponseEntity<UserResponse> login(String loginId, String password);

    ResponseEntity<UserResponse> getByUserName(String token, String userName);

    boolean validateToken(String token);
}
