package com.tweetapp.service;

import com.tweetapp.entity.UserEntity;
import com.tweetapp.model.AuthResponse;
import com.tweetapp.model.User;
import com.tweetapp.model.UserResponse;
import com.tweetapp.repository.UserRepository;
import com.tweetapp.utils.EntityModelMapper;
import com.tweetapp.utils.ServiceConstants;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.protocol.types.Field;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SequenceService sequenceService;
    @Autowired
    private JwtUtil jwtutil;

    /**
     * To register user
     *
     * @param user
     * @return UserResponse
     */
    @Override
    public ResponseEntity<UserResponse> registerUser(User user) {
        try {
            if (!user.getConfirmPassword().equals(user.getPassword())) {
                return new ResponseEntity<>(UserResponse.builder().message(ServiceConstants.PASSWORD_NOT_MATCHED)
                        .messageCode(HttpStatus.CONFLICT)
                        .messageType(ServiceConstants.FAILURE)
                        .build(), HttpStatus.CONFLICT);
            }
            Optional<UserEntity> optionalUserLoginCheck = userRepository.findByLoginId(user.getLoginId());
            if (optionalUserLoginCheck.isPresent()) {
                return new ResponseEntity<>(UserResponse.builder().message(ServiceConstants.LOGIN_USED)
                        .messageCode(HttpStatus.CONFLICT)
                        .messageType(ServiceConstants.FAILURE)
                        .build(), HttpStatus.CONFLICT);
            }
            Optional<UserEntity> optionalUserEmailCheck = userRepository.findByEmail(user.getEmail());
            if (optionalUserEmailCheck.isPresent()) {
                return new ResponseEntity<>(UserResponse.builder().message(ServiceConstants.EMAIL_USED)
                        .messageCode(HttpStatus.CONFLICT)
                        .messageType(ServiceConstants.FAILURE)
                        .build(), HttpStatus.CONFLICT);
            }
            UserEntity userEntity = EntityModelMapper.userToUserEntity(user);
            userEntity.setUserId(sequenceService.getNextSequence(UserEntity.SEQUENCE_NAME));
            userRepository.save(userEntity);
            return new ResponseEntity<>(UserResponse.builder().message(ServiceConstants.ID_CREATED)
                    .messageCode(HttpStatus.OK)
                    .messageType(ServiceConstants.SUCCESS)
                    .build(), HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error while creating user {}", e.getMessage());
        }
        return new ResponseEntity<>(UserResponse.builder().message(ServiceConstants.FAILURE)
                .messageCode(HttpStatus.INTERNAL_SERVER_ERROR)
                .messageType(ServiceConstants.FAILURE)
                .build(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Used for login
     *
     * @param loginId
     * @param password
     * @return UserResponse
     */
    @Override
    public ResponseEntity<UserResponse> login(String loginId, String password) {
        try {
            Optional<UserEntity> optionalUserLoginCheck = userRepository.findByLoginId(loginId);
            String generateToken = "";
            if (optionalUserLoginCheck.isPresent()) {
                UserEntity userEntity = optionalUserLoginCheck.get();
                if (userEntity.getPassword().equals(password)) {
                    generateToken = jwtutil.generateToken(userEntity.getLoginId());
                    return new ResponseEntity<>(UserResponse.builder().message(ServiceConstants.LOGIN_SUCCESS)
                            .authResponse(AuthResponse.builder().authToken(generateToken).userName(loginId).isValid(true).build())
                            .messageCode(HttpStatus.OK)
                            .messageType(ServiceConstants.SUCCESS)
                            .build(), HttpStatus.OK);
                }
                return new ResponseEntity<>(UserResponse.builder().message(ServiceConstants.PASSWORD_WRONG)
                        .messageCode(HttpStatus.CONFLICT)
                        .messageType(ServiceConstants.FAILURE)
                        .build(), HttpStatus.CONFLICT);
            }
            return new ResponseEntity<>(UserResponse.builder().message(ServiceConstants.USER_NOT_EXIST)
                    .messageCode(HttpStatus.CONFLICT)
                    .messageType(ServiceConstants.FAILURE)
                    .build(), HttpStatus.CONFLICT);
        } catch (Exception e) {
            log.error("Error while login {}", e.getMessage());
        }
        return new ResponseEntity<>(UserResponse.builder().message(ServiceConstants.FAILURE)
                .messageCode(HttpStatus.INTERNAL_SERVER_ERROR)
                .messageType(ServiceConstants.FAILURE)
                .build(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * For forgot password
     *
     * @param userName
     * @return UserResponse
     */
    @Override
    public ResponseEntity<UserResponse> forgotPassword(String userName) {
        try {
            Optional<UserEntity> optionalUserLoginCheck = userRepository.findByLoginId(userName);
            if (optionalUserLoginCheck.isPresent()) {
                return new ResponseEntity<>(UserResponse.builder().message(ServiceConstants.SUCCESS)
                        .messageCode(HttpStatus.OK)
                        .messageType(ServiceConstants.SUCCESS)
                        .build(), HttpStatus.OK);
            }
            return new ResponseEntity<>(UserResponse.builder().message(ServiceConstants.USER_NOT_EXIST)
                    .messageCode(HttpStatus.CONFLICT)
                    .messageType(ServiceConstants.FAILURE)
                    .build(), HttpStatus.CONFLICT);
        } catch (Exception e) {
            log.error("Error while forgotPassword {}", e.getMessage());
        }
        return new ResponseEntity<>(UserResponse.builder().message(ServiceConstants.FAILURE)
                .messageCode(HttpStatus.INTERNAL_SERVER_ERROR)
                .messageType(ServiceConstants.FAILURE)
                .build(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * To reset Password
     *
     * @param userName
     * @param user
     * @return UserResponse
     */
    @Override
    public ResponseEntity<UserResponse> resetPassword(String userName, User user) {
        try {
            Optional<UserEntity> optionalUserLoginCheck = userRepository.findByLoginId(userName);
            if (optionalUserLoginCheck.isPresent()) {
                if (!user.getConfirmPassword().equals(user.getPassword())) {
                    return new ResponseEntity<>(UserResponse.builder().message(ServiceConstants.PASSWORD_NOT_MATCHED)
                            .messageCode(HttpStatus.CONFLICT)
                            .messageType(ServiceConstants.FAILURE)
                            .build(), HttpStatus.CONFLICT);
                }
                UserEntity userEntity = optionalUserLoginCheck.get();
                if (userEntity.getEmail().equals(user.getEmail()) && userEntity.getContactNumber() == user.getContactNumber()) {
                    userEntity.setPassword(user.getPassword());
                    userRepository.save(userEntity);
                    return new ResponseEntity<>(UserResponse.builder().message(ServiceConstants.PASSWORD_CHANGED)
                            .messageCode(HttpStatus.OK)
                            .messageType(ServiceConstants.SUCCESS)
                            .build(), HttpStatus.OK);
                }
            }
            return new ResponseEntity<>(UserResponse.builder().message(ServiceConstants.PHONE_EMAIL_NOT_MATCH)
                    .messageCode(HttpStatus.CONFLICT)
                    .messageType(ServiceConstants.FAILURE)
                    .build(), HttpStatus.CONFLICT);
        } catch (Exception e) {
            log.error("Error while Reset Password {}", e.getMessage());
        }
        return new ResponseEntity<>(UserResponse.builder().message(ServiceConstants.FAILURE)
                .messageCode(HttpStatus.INTERNAL_SERVER_ERROR)
                .messageType(ServiceConstants.FAILURE)
                .build(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * To get all users
     *
     * @param token
     * @return TweetResponse
     */
    @Override
    public ResponseEntity<UserResponse> getAllUsers(String token) {
        try {
            if (validateToken(token)) {
                List<UserEntity> userEntityList = userRepository.findAll();
                if (userEntityList.isEmpty()) {
                    return new ResponseEntity<>(UserResponse.builder().message(ServiceConstants.USER_NOT_EXIST)
                            .messageCode(HttpStatus.NOT_FOUND)
                            .messageType(ServiceConstants.FAILURE)
                            .build(), HttpStatus.NOT_FOUND);
                }
                List<User> userList = new ArrayList<>();
                for (UserEntity userEntity : userEntityList) {
                    userList.add(EntityModelMapper.userEntityToUser(userEntity));
                }
                return new ResponseEntity<>(UserResponse.builder().message(ServiceConstants.SUCCESS)
                        .userList(userList)
                        .messageCode(HttpStatus.OK)
                        .messageType(ServiceConstants.SUCCESS)
                        .build(), HttpStatus.OK);
            } else {
                new ResponseEntity<>(UserResponse.builder().message(ServiceConstants.FAILURE)
                        .messageCode(HttpStatus.FORBIDDEN)
                        .messageType(ServiceConstants.FAILURE)
                        .build(), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (Exception e) {
            log.error("Error while get all User {}", e.getMessage());
        }
        return new ResponseEntity<>(UserResponse.builder().message(ServiceConstants.FAILURE)
                .messageCode(HttpStatus.INTERNAL_SERVER_ERROR)
                .messageType(ServiceConstants.FAILURE)
                .build(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * To search user based on Username
     *
     * @param token
     * @param userName
     * @return UserResponse
     */
    @Override
    public ResponseEntity<UserResponse> searchByUserName(String token, String userName) {
        try {
            if (validateToken(token)) {
                List<UserEntity> userEntityList = userRepository.findByLoginIdLike(userName);
                if (userEntityList.isEmpty()) {
                    return new ResponseEntity<>(UserResponse.builder().message(ServiceConstants.USER_NOT_EXIST)
                            .messageCode(HttpStatus.NOT_FOUND)
                            .messageType(ServiceConstants.FAILURE)
                            .build(), HttpStatus.NOT_FOUND);
                }
                List<User> userList = new ArrayList<>();
                for (UserEntity userEntity : userEntityList) {
                    userList.add(EntityModelMapper.userEntityToUser(userEntity));
                }
                return new ResponseEntity<>(UserResponse.builder().message(ServiceConstants.SUCCESS)
                        .userList(userList)
                        .messageCode(HttpStatus.OK)
                        .messageType(ServiceConstants.SUCCESS)
                        .build(), HttpStatus.OK);
            } else {
                new ResponseEntity<>(UserResponse.builder().message(ServiceConstants.FAILURE)
                        .messageCode(HttpStatus.FORBIDDEN)
                        .messageType(ServiceConstants.FAILURE)
                        .build(), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (Exception e) {
            log.error("Error while search by user Name {}", e.getMessage());
        }
        return new ResponseEntity<>(UserResponse.builder().message(ServiceConstants.FAILURE)
                .messageCode(HttpStatus.INTERNAL_SERVER_ERROR)
                .messageType(ServiceConstants.FAILURE)
                .build(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * To search user based on Username
     *
     * @param token
     * @param userName
     * @return UserResponse
     */
    @Override
    public ResponseEntity<UserResponse> getByUserName(String token, String userName) {
        try {
            if (validateToken(token)) {
                Optional<UserEntity> userEntityList = userRepository.findByLoginId(userName);
                if (userEntityList.isEmpty()) {
                    return new ResponseEntity<>(UserResponse.builder().message(ServiceConstants.USER_NOT_EXIST)
                            .messageCode(HttpStatus.NOT_FOUND)
                            .messageType(ServiceConstants.FAILURE)
                            .build(), HttpStatus.NOT_FOUND);
                }
                List<User> userList = new ArrayList<>();
                userList.add(EntityModelMapper.userEntityToUser(userEntityList.get()));

                return new ResponseEntity<>(UserResponse.builder().message(ServiceConstants.SUCCESS)
                        .userList(userList)
                        .messageCode(HttpStatus.OK)
                        .messageType(ServiceConstants.SUCCESS)
                        .build(), HttpStatus.OK);
            } else {
                new ResponseEntity<>(UserResponse.builder().message(ServiceConstants.FAILURE)
                        .messageCode(HttpStatus.FORBIDDEN)
                        .messageType(ServiceConstants.FAILURE)
                        .build(), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (Exception e) {
            log.error("Error while get by user Name {}", e.getMessage());
        }
        return new ResponseEntity<>(UserResponse.builder().message(ServiceConstants.FAILURE)
                .messageCode(HttpStatus.INTERNAL_SERVER_ERROR)
                .messageType(ServiceConstants.FAILURE)
                .build(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * To validate token
     *
     * @param token
     * @return AuthResponse
     */
    @Override
    public boolean validateToken(String token) {
        boolean res;
        try {
            String token1 = token.substring(7);
            res = jwtutil.validateToken(token1);
        } catch (Exception e) {
            res = false;
            log.error("Token Expired {}", e.getMessage());
        }
        return res;
    }
}
