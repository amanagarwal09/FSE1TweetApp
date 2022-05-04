package com.tweetapp.repository;

import com.tweetapp.entity.UserEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<UserEntity, Integer> {

    Optional<UserEntity> findByLoginId(String loginId);

    Optional<UserEntity> findByEmail(String email);

    List<UserEntity> findByLoginIdLike(String loginId);
}