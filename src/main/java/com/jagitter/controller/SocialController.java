package com.jagitter.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.jagitter.data.ErrorMessage;
import com.jagitter.data.User;
import com.jagitter.service.UserService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/social")
@Api(value = "social")
public class SocialController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @RequestMapping(
            value = "/{user-id-1}/follows/{user-id-2}",
            method = RequestMethod.PUT)
    @ApiOperation(
            value = "User 1 follows User 2",
            notes = "User 1 follows User 2")
    @ResponseStatus(code=HttpStatus.CREATED)
    public void follows(
            @PathVariable("user-id-1") String followingUserId,
            @PathVariable("user-id-2") String followedUserId) {

        if (followingUserId.equals(followedUserId)) {
            throw new SocialControllerException("User can't follow itself");
        }

        if (!userService.userExists(followingUserId)) {
            throw new SocialControllerException("User with id '" + followingUserId + "' doesn't exist");
        }

        if (!userService.userExists(followedUserId)) {
            throw new SocialControllerException("User with id '" + followedUserId + "' doesn't exist");
        }

        userService.follow(followingUserId, followedUserId);
    }

    @RequestMapping(
            value = "/{user-id}/follows",
            method = RequestMethod.GET)
    @ApiOperation(
            value = "Show user's follows",
            notes = "Check who user follows")
    @ResponseStatus(code=HttpStatus.OK)
    public ResponseEntity<List<User>> getFollowedUsers(
            @PathVariable("user-id") String userId) {

        if (!userService.userExists(userId)) {
            throw new SocialControllerException("User with id '" + userId + "' doesn't exist");
        }

        List<User> users = userService.getFollowedUsers(userId);
        return ResponseEntity.ok().body(users);
    }

    @RequestMapping(
            value = "/{user-id}/followed-by",
            method = RequestMethod.GET)
    @ApiOperation(
            value = "Show user's followers",
            notes = "Check who is following user")
    public ResponseEntity<List<User>> getFollowingUsers(
            @PathVariable("user-id") String userId) {

        if (!userService.userExists(userId)) {
            throw new SocialControllerException("User with id '" + userId + "' doesn't exist");
        }

        List<User> users = userService.getFollowingUsers(userId);
        return ResponseEntity.ok().body(users);
    }

    private class SocialControllerException extends RuntimeException {
        private static final long serialVersionUID = 1L;

        public SocialControllerException(String msg) {
            super(msg);
        }
    }

    @ExceptionHandler(SocialControllerException.class)
    @ResponseBody
    public ResponseEntity<ErrorMessage> handleException(SocialControllerException exception) {
        logger.info("handleException");

        return ResponseEntity.badRequest()
                .body(new ErrorMessage(exception.getMessage()));
    }
}
