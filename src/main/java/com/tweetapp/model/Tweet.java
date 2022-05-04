package com.tweetapp.model;

import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Tweet {

    private Integer userId;
    private Integer tweetId;
    @Length(max = 144, message = "Tweet should not exceed the length of 144")
    @NotEmpty(message = "Tweet should not be empty")
    private String tweetDesc;
    private LocalDateTime createdDate;
    private Integer parentTweetId;
    private Integer likeCount;

}
