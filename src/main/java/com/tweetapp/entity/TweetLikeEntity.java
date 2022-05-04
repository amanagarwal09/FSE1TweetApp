package com.tweetapp.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
@Document(collection = "tweetlike")
public class TweetLikeEntity implements Serializable {


    @Transient
    public static final String SEQUENCE_NAME = "tweetlike";
    private static final long serialVersionUID = 955728933773177564L;
    @Id
    private Integer tweetLikeId;
    private Integer userId;
    private Integer tweetId;
}
