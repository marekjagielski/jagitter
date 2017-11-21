package com.jagitter.service;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jagitter.data.User;
import com.jagitter.repository.UserRepository;

@Service("userService")
public class UserServiceImpl implements UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UserRepository repository;


    @Override
    public boolean userExists(String userId) {
        return repository.exists(userId);
    }

    @Override
    public User createUser(String userId) {
        return repository.save(new User(userId));
    }

    @Override
    public void deleteUser(String userId) {
        repository.delete(userId);
    }

    @Override
    public List<User> listAllUsers() {
        return repository.findAll();
    }

    @Override
    public void follow(String followingUserId, String followedUserId) {
        logger.info("follow({}, {})", followingUserId, followedUserId);
        User followingUser = repository.findOne(followingUserId);
        User followedUser = repository.findOne(followedUserId);
        followingUser.getFollowed().add(followedUser);
        repository.save(followingUser);
    }

    @Override
    public void unfollow(String followingUserId, String followedUserId) {
        User followingUser = repository.findOne(followingUserId);
        Optional<User> followedUser = followingUser.getFollowed().stream()
                .filter(u -> followedUserId.equals(u.getUserId()))
                .findFirst();

        if (followedUser.isPresent()) {
            followingUser.getFollowed().remove(followedUser.get());
        }

        repository.save(followingUser);
    }

    @Override
    public List<User> getFollowedUsers(String userId) {
        User user = repository.findOne(userId);
        return user.getFollowed();
    }

    @Override
    public List<User> getFollowingUsers(String userId) {
        return repository.findUsersThatFollow(userId);
    }
}
