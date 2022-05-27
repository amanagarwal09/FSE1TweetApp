package com.tweetapp.service;

import com.tweetapp.model.User;
import com.tweetapp.model.UserResponse;
import com.tweetapp.repository.UserRepository;
import com.tweetapp.util.TestUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    UserServiceImpl userService;

    @Mock
    UserRepository userRepository;

    @Mock
    SequenceService sequenceService;
    @Mock
    JwtUtil jwtutil;

    @Test
    void testRegisterUserPasswordConflict() {
        User user = TestUtil.sampleUser();
        user.setConfirmPassword("test1");
        ResponseEntity<UserResponse> response = userService.registerUser(user);
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }

    @Test
    void testRegisterUserConflict() {
        User user = TestUtil.sampleUser();
        user.setLoginId("akash");
        when(userRepository.findByLoginId(Mockito.anyString()))
                .thenReturn(Optional.of(TestUtil.sampleUserEntity()));
        ResponseEntity<UserResponse> response = userService.registerUser(user);
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }

    @Test
    void testRegisterUserEmailConflict() {
        User user = TestUtil.sampleUser();
        user.setLoginId("akash");
        user.setEmail("akash@testmail.com");
        when(userRepository.findByLoginId(Mockito.anyString()))
                .thenReturn(Optional.empty());
        when(userRepository.findByEmail(Mockito.anyString()))
                .thenReturn(Optional.of(TestUtil.sampleUserEntity()));
        ResponseEntity<UserResponse> response = userService.registerUser(user);
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }

    @Test
    void testRegisterUser() {
        User user = TestUtil.sampleUser();
        user.setLoginId("akash");
        user.setEmail("akash@testmail.com");
        when(userRepository.findByLoginId(Mockito.anyString()))
                .thenReturn(Optional.empty());
        when(userRepository.findByEmail(Mockito.anyString()))
                .thenReturn(Optional.empty());
        when(sequenceService.getNextSequence(Mockito.anyString()))
                .thenReturn(123);
        ResponseEntity<UserResponse> response = userService.registerUser(user);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testRegisterUserException() {
        User user = TestUtil.sampleUser();
        user.setLoginId("akash");
        user.setEmail("akash@testmail.com");
        when(userRepository.findByLoginId(Mockito.anyString()))
                .thenThrow(NullPointerException.class);
        ResponseEntity<UserResponse> response = userService.registerUser(user);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    void testLogin() {
        when(userRepository.findByLoginId("aman"))
                .thenReturn(Optional.of(TestUtil.sampleUserEntity()));
        ResponseEntity<UserResponse> response = userService.login("aman", TestUtil.sampleUserEntity().getPassword());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testLoginWrongPassword() {
        when(userRepository.findByLoginId("aman"))
                .thenReturn(Optional.of(TestUtil.sampleUserEntity()));
        ResponseEntity<UserResponse> response = userService.login("aman", "wrong-password");
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }

    @Test
    void testLoginWrongLoginId() {
        when(userRepository.findByLoginId(Mockito.anyString()))
                .thenReturn(Optional.empty());
        ResponseEntity<UserResponse> response = userService.login("akash", "wrong-password");
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }

    @Test
    void testLoginException() {
        when(userRepository.findByLoginId(Mockito.anyString()))
                .thenThrow(NullPointerException.class);
        ResponseEntity<UserResponse> response = userService.login("akash", "wrong-password");
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    void testForgotPassword() {
        when(userRepository.findByLoginId(Mockito.anyString()))
                .thenReturn(Optional.of(TestUtil.sampleUserEntity()));
        ResponseEntity<UserResponse> response = userService.forgotPassword(Mockito.anyString());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testForgotPasswordUserDoesNotExist() {
        when(userRepository.findByLoginId(Mockito.anyString()))
                .thenReturn(Optional.empty());
        ResponseEntity<UserResponse> response = userService.forgotPassword(Mockito.anyString());
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }

    @Test
    void testForgotPasswordException() {
        when(userRepository.findByLoginId(Mockito.anyString()))
                .thenThrow(NullPointerException.class);
        ResponseEntity<UserResponse> response = userService.forgotPassword(Mockito.anyString());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    void testResetPasswordPassword() {
        when(userRepository.findByLoginId(Mockito.anyString()))
                .thenReturn(Optional.of(TestUtil.sampleUserEntity()));
        ResponseEntity<UserResponse> response = userService.resetPassword("Aman", User.builder()
                .loginId(TestUtil.sampleUserEntity().getLoginId())
                .email(TestUtil.sampleUserEntity().getEmail())
                .firstName("test")
                .lastName("test")
                .password("test")
                .confirmPassword("test1")
                .contactNumber(TestUtil.sampleUserEntity().getContactNumber())
                .build());
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }

    @Test
    void testResetPassword() {
        when(userRepository.findByLoginId(Mockito.anyString()))
                .thenReturn(Optional.of(TestUtil.sampleUserEntity()));
        ResponseEntity<UserResponse> response = userService.resetPassword("Aman", User.builder()
                .loginId(TestUtil.sampleUserEntity().getLoginId())
                .email(TestUtil.sampleUserEntity().getEmail())
                .firstName("test")
                .lastName("test")
                .password("test")
                .confirmPassword("test")
                .contactNumber(TestUtil.sampleUserEntity().getContactNumber())
                .build());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testResetPasswordEmailNotMatched() {
        when(userRepository.findByLoginId(Mockito.anyString()))
                .thenReturn(Optional.of(TestUtil.sampleUserEntity()));
        ResponseEntity<UserResponse> response = userService.resetPassword("Aman", User.builder()
                .loginId(TestUtil.sampleUserEntity().getLoginId())
                .email("wrong@mail.com")
                .firstName("test")
                .lastName("test")
                .password("test")
                .confirmPassword("test")
                .contactNumber(TestUtil.sampleUserEntity().getContactNumber())
                .build());
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }


    @Test
    void testResetPasswordContactNotMatched() {
        when(userRepository.findByLoginId(Mockito.anyString()))
                .thenReturn(Optional.of(TestUtil.sampleUserEntity()));
        ResponseEntity<UserResponse> response = userService.resetPassword("Aman", User.builder()
                .loginId(TestUtil.sampleUserEntity().getLoginId())
                .email(TestUtil.sampleUserEntity().getEmail())
                .firstName("test")
                .lastName("test")
                .password("test")
                .confirmPassword("test")
                .contactNumber(1234567890)
                .build());
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }

    @Test
    void testResetPasswordLoginIdNotFound() {
        when(userRepository.findByLoginId(Mockito.anyString()))
                .thenReturn(Optional.empty());
        ResponseEntity<UserResponse> response = userService.resetPassword("Aman", User.builder()
                .loginId(TestUtil.sampleUserEntity().getLoginId())
                .email(TestUtil.sampleUserEntity().getEmail())
                .contactNumber(TestUtil.sampleUserEntity().getContactNumber())
                .build());
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }

    @Test
    void testResetPasswordException() {
        when(userRepository.findByLoginId(Mockito.anyString()))
                .thenThrow(NullPointerException.class);
        ResponseEntity<UserResponse> response = userService.resetPassword("Aman", User.builder()
                .loginId(TestUtil.sampleUserEntity().getLoginId())
                .email(TestUtil.sampleUserEntity().getEmail())
                .contactNumber(TestUtil.sampleUserEntity().getContactNumber())
                .build());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    void testGetAllUsers() {
        when(jwtutil.validateToken(Mockito.anyString())).thenReturn(true);
        when(userRepository.findAll())
                .thenReturn(Collections.singletonList(TestUtil.sampleUserEntity()));
        ResponseEntity<UserResponse> response = userService.getAllUsers("tokentokentokentoken");
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testGetAllUsersNotFound() {
        when(jwtutil.validateToken(Mockito.anyString())).thenReturn(true);
        when(userRepository.findAll())
                .thenReturn(Collections.emptyList());
        ResponseEntity<UserResponse> response = userService.getAllUsers("tokentokentokentoken");
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testGetAllUsersException() {
        when(jwtutil.validateToken(Mockito.anyString())).thenReturn(true);
        when(userRepository.findAll())
                .thenThrow(NullPointerException.class);
        ResponseEntity<UserResponse> response = userService.getAllUsers("tokentokentokentokentoken");
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    void testSearchByUserName() {
        when(jwtutil.validateToken(Mockito.anyString())).thenReturn(true);
        when(userRepository.findByLoginIdLike(Mockito.anyString()))
                .thenReturn(Collections.singletonList(TestUtil.sampleUserEntity()));
        ResponseEntity<UserResponse> response = userService.searchByUserName("tokentokentokentoken", "A");
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testSearchByUserNameNotFound() {
        when(jwtutil.validateToken(Mockito.anyString())).thenReturn(true);
        when(userRepository.findByLoginIdLike(Mockito.anyString()))
                .thenReturn(Collections.emptyList());
        ResponseEntity<UserResponse> response = userService.searchByUserName("tokentokentokentoken", "A");
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testSearchByUserNameException() {
        when(jwtutil.validateToken(Mockito.anyString())).thenReturn(true);
        when(userRepository.findByLoginIdLike(Mockito.anyString()))
                .thenThrow(NullPointerException.class);
        ResponseEntity<UserResponse> response = userService.searchByUserName("tokentokentokentoken", "A");
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }
}
