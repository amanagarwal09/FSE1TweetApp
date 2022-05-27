package com.tweetapp.model;

import lombok.*;
import org.springframework.http.HttpStatus;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class UserResponse {

    private List<User> userList;
    private AuthResponse authResponse;
    private String messageType;
    private HttpStatus messageCode;
    private String message;


}
