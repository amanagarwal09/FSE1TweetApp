package com.tweetapp.model;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {
    private String userName;
    private boolean isValid;
    private String authToken;

}
