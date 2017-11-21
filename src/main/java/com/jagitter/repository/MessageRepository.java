package com.jagitter.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.jagitter.data.Message;

@Repository
public interface MessageRepository extends MongoRepository<Message, String> {

    Page<Message> findByUserId(String userId, Pageable pageable);

    @Query("{ 'userId': { $in: ?0 } }")
    Page<Message> findByUserIds(String[] usersIds, Pageable pageable);
}
