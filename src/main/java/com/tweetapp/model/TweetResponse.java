package com.tweetapp.model;

import lombok.*;
import org.springframework.http.HttpStatus;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor

public class TweetResponse {

    private List<Tweet> tweetList;
    private String messageType;
    private HttpStatus messageCode;
    private String message;


}
