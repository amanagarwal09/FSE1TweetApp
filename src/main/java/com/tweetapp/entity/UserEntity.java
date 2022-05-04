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
@Document(collection = "user")
public class UserEntity implements Serializable {

    @Transient
    public static final String SEQUENCE_NAME = "user";
    private static final long serialVersionUID = 955728933773177564L;
    @Id
    private Integer userId;
    private String firstName;
    private String lastName;
    private String email;
    private String loginId;
    private String password;
    private long contactNumber;
}
