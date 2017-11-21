package com.jagitter.service;

import java.util.List;

import com.jagitter.data.User;

public interface UserService {

    boolean userExists(String userId);

    User createUser(String userId);

    void deleteUser(String userId);

    List<User> listAllUsers();

    void follow(String followingUserId, String followedUserId);

    void unfollow(String followingUserId, String followedUserId);

    List<User> getFollowedUsers(String userId);

    List<User> getFollowingUsers(String userId);

}
