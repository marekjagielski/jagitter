package com.jagitter.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
@RequestMapping("/users")
@Api(value = "user")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;


    @RequestMapping(
            value = "/{user-id}",
            method = RequestMethod.PUT)
    @ApiOperation(
            value = "Create a new user with user-id",
            notes = "Create a new user with user-id")
    @ResponseStatus(code=HttpStatus.CREATED)
    public void createUser(
            @PathVariable("user-id") String userId) {
        logger.info("createUser");

        if(userService.userExists(userId)) {
            throw new UserControllerException("User with id '" + userId + "' already exists");
        }

        User createdUser = userService.createUser(userId);
        if (createdUser == null) {
            logger.error("User '{}' not created", userId);
        }
    }


    @RequestMapping(
            value = "/{user-id}",
            method = RequestMethod.DELETE)
    @ApiOperation(
            value = "Delete user with user-id",
            notes = "Delete user with user-id")
    @ResponseStatus(code=HttpStatus.OK)
    public void deleteUser(
            @PathVariable("user-id") String userId) {
        logger.info("deleteUser");
        userService.deleteUser(userId);
    }


    @RequestMapping(
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE,
            method = RequestMethod.GET)
    @ApiOperation(
            value = "List all users",
            notes = "List all users")
    public ResponseEntity<List<User>> listAllUsers() {
        List<User> usersList = userService.listAllUsers();

        if (usersList.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok().body(usersList);
    }


    private class UserControllerException extends RuntimeException {
        private static final long serialVersionUID = 1L;

        public UserControllerException(String msg) {
            super(msg);
        }
    }


    @ExceptionHandler(UserControllerException.class)
    @ResponseBody
    public ResponseEntity<ErrorMessage> handleException(UserControllerException exception) {
        logger.info("handleException");

        return ResponseEntity.badRequest()
                .body(new ErrorMessage(exception.getMessage()));
    }
}
