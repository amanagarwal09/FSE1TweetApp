package com.tweetapp.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
@Document(collection = "tweet")
public class TweetEntity implements Serializable {

    @Transient
    public static final String SEQUENCE_NAME = "tweet";
    private static final long serialVersionUID = 955728933773177564L;
    @Id
    private Integer tweetId;
    private Integer userId;
    private String tweetDesc;
    private LocalDateTime createdDate;
    private Integer parentTweetId;
}
