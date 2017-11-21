package com.jagitter.data;

import java.util.LinkedList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class User {

    @Id
    private String userId;

    @JsonIgnore
    @DBRef
    private List<User> followed = new LinkedList<>();

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<User> getFollowed() {
        return followed;
    }

    public void setFollowed(List<User> followed) {
        this.followed = followed;
    }

    public User() {}

    public User(String userId) {
        this.userId = userId;
    }
}
