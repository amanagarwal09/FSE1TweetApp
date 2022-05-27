package com.tweetapp.controller;

import com.tweetapp.model.Tweet;
import com.tweetapp.model.TweetResponse;
import com.tweetapp.model.User;
import com.tweetapp.model.UserResponse;
import com.tweetapp.service.TweetService;
import com.tweetapp.service.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles(profiles = "test")
@AutoConfigureMockMvc
class TweetAppControllerTest {

    @Autowired
    MockMvc mockMvc;

    @InjectMocks
    TweetAppController tweetAppController;

    @MockBean
    TweetService tweetService;

    @MockBean
    UserServiceImpl userService;

    @Test
    void testRegisterUser() throws Exception {
        when(userService.registerUser(Mockito.any(User.class))).thenReturn(ResponseEntity.ok(UserResponse.builder().messageCode(HttpStatus.OK).message("Success").build()));
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/v1.0/tweets/register").content("{\n" +
                "  \"confirmPassword\": \"string\",\n" +
                "  \"contactNumber\": 12345678,\n" +
                "  \"email\": \"string@gmail.com\",\n" +
                "  \"firstName\": \"string\",\n" +
                "  \"lastName\": \"string\",\n" +
                "  \"loginId\": \"string\",\n" +
                "  \"password\": \"string\",\n" +
                "  \"userId\": 1\n" +
                "}").contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(requestBuilder).andExpect(status().isOk());
    }

    @Test
    void testLogin() throws Exception {
        when(userService.login(Mockito.any(), Mockito.any())).thenReturn(ResponseEntity.ok(UserResponse.builder().messageCode(HttpStatus.OK).message("Success").build()));
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/v1.0/tweets/login").param("loginId", "akash").param("password", "password");
        mockMvc.perform(requestBuilder).andExpect(status().isOk());
    }

    @Test
    void testForgotPassword() throws Exception {
        when(userService.forgotPassword(Mockito.any())).thenReturn(new ResponseEntity<>(UserResponse.builder().messageCode(HttpStatus.OK).message("Success").build(), HttpStatus.OK));
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/v1.0/tweets/testuser/forgot").contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(requestBuilder).andExpect(status().isOk());
    }

    @Test
    void testResetPassword() throws Exception {
        when(userService.resetPassword(Mockito.any(), Mockito.any())).thenReturn(new ResponseEntity<>(UserResponse.builder().messageCode(HttpStatus.OK).message("Success").build(), HttpStatus.OK));
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/v1.0/tweets/testuser/resetpassword").content("{\"loginId\":\"akash\",\"email\":\"testemail@mail.com\"}").contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(requestBuilder).andExpect(status().isOk());
    }

    @Test
    void testGetAllTweets() throws Exception {
        when(tweetService.getAllTweets(Mockito.anyString())).thenReturn(ResponseEntity.ok(TweetResponse.builder().messageCode(HttpStatus.OK).message("Success").build()));
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/v1.0/tweets/all").header("Authorization", "AuthorizationAuthorizationAuthorization");
        mockMvc.perform(requestBuilder).andExpect(status().isOk());
    }

    @Test
    void testSearchByUserName() throws Exception {
        when(userService.searchByUserName(Mockito.anyString(), Mockito.anyString())).thenReturn(ResponseEntity.ok(UserResponse.builder().messageCode(HttpStatus.OK).message("Success").build()));
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/v1.0/tweets/user/search/testuser").header("Authorization", "AuthorizationAuthorizationAuthorization");
        mockMvc.perform(requestBuilder).andExpect(status().isOk());
    }

    @Test
    void testGetAllTweetsOfUser() throws Exception {
        when(tweetService.getAllTweetsOfUser(Mockito.anyString(), Mockito.anyString())).thenReturn(ResponseEntity.ok(TweetResponse.builder().messageCode(HttpStatus.OK).message("Success").build()));
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/v1.0/tweets/testuser")
                .header("Authorization", "AuthorizationAuthorizationAuthorization");
        mockMvc.perform(requestBuilder).andExpect(status().isOk());
    }

    @Test
    void testPostNewTweet() throws Exception {
        when(tweetService.postNewTweet(Mockito.anyString(), Mockito.anyString(), Mockito.any(Tweet.class))).thenReturn(ResponseEntity.ok(TweetResponse.builder().messageCode(HttpStatus.OK).message("Success").build()));
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/v1.0/tweets/testuser/add").content("{\"userId\":12,\"tweetId\":32,\"tweetDesc\":\"Testing\"}").contentType(MediaType.APPLICATION_JSON).header("Authorization", "AuthorizationAuthorizationAuthorization");
        mockMvc.perform(requestBuilder).andExpect(status().isOk());
    }

    @Test
    void testUpdateTweet() throws Exception {
        when(tweetService.updateTweet(Mockito.anyString(), Mockito.anyString(), Mockito.anyInt(), Mockito.any(Tweet.class))).thenReturn(ResponseEntity.ok(TweetResponse.builder().messageCode(HttpStatus.OK).message("Success").build()));
        RequestBuilder requestBuilder = MockMvcRequestBuilders.put("/api/v1.0/tweets/testuser/update/12")
                .content("{\"userId\":12,\"tweetId\":32,\"tweetDesc\":\"Testing\"}").contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "AuthorizationAuthorizationAuthorization");
        mockMvc.perform(requestBuilder).andExpect(status().isOk());
    }

    @Test
    void testDeleteTweet() throws Exception {
        when(tweetService.deleteTweet(Mockito.anyString(), Mockito.anyString(), Mockito.anyInt())).thenReturn(ResponseEntity.ok(TweetResponse.builder().messageCode(HttpStatus.OK).message("Success").build()));
        RequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/api/v1.0/tweets/testuser/delete/12")
                .header("Authorization", "AuthorizationAuthorizationAuthorization");
        mockMvc.perform(requestBuilder).andExpect(status().isOk());
    }

    @Test
    void testLikeTweet() throws Exception {
        when(tweetService.likeTweet(Mockito.anyString(), Mockito.anyString(), Mockito.anyInt())).thenReturn(ResponseEntity.ok(TweetResponse.builder().messageCode(HttpStatus.OK).message("Success").build()));
        RequestBuilder requestBuilder = MockMvcRequestBuilders.put("/api/v1.0/tweets/testuser/like/12").header("Authorization", "AuthorizationAuthorizationAuthorization");
        mockMvc.perform(requestBuilder).andExpect(status().isOk());
    }

    @Test
    void testGetAllUsers() throws Exception {
        when(userService.getAllUsers(Mockito.anyString())).thenReturn(ResponseEntity.ok(UserResponse.builder().messageCode(HttpStatus.OK).message("Success").build()));
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/v1.0/tweets/users/all").header("Authorization", "AuthorizationAuthorizationAuthorization");
        mockMvc.perform(requestBuilder).andExpect(status().isOk());
    }

    @Test
    void testReplyToTweet() throws Exception {
        when(tweetService.replyToTweet(Mockito.anyString(), Mockito.anyString(), Mockito.anyInt(), Mockito.any(Tweet.class))).thenReturn(ResponseEntity.ok(TweetResponse.builder().messageCode(HttpStatus.OK).message("Success").build()));
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/v1.0/tweets/testuser/reply/12").content("{\"tweetDesc\":\"Testing\"}").contentType(MediaType.APPLICATION_JSON).header("Authorization", "AuthorizationAuthorizationAuthorization");
        mockMvc.perform(requestBuilder).andExpect(status().isOk());
    }
}
