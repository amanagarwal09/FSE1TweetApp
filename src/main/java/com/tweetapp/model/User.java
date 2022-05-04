package com.tweetapp.model;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class User {

    private Integer userId;
    @NotEmpty(message = "First Name should not be empty")
    private String firstName;
    @NotEmpty(message = "Last Name should not be empty")
    private String lastName;
    @NotEmpty(message = "Email should not be empty")
    @Email(message = "Email should be valid")
    private String email;
    @NotEmpty(message = "Login Id should not be empty")
    private String loginId;
    @NotEmpty(message = "Password should not be empty")
    private String password;
    @NotEmpty(message = "Confirm Password cannot be empty")
    private String confirmPassword;
    @Min(value = 1, message = "Contact Number should not be empty")
    private long contactNumber;


}
