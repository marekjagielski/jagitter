package com.jagitter.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.jagitter.data.User;

@Repository
public interface UserRepository extends MongoRepository<User, String> {

    @Query("{ 'followed' : ?0 }")
    List<User> findUsersThatFollow(String userId);
}
