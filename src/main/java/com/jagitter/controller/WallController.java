package com.jagitter.controller;

import java.util.List;
import java.util.Optional;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.constraints.Size;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.jagitter.data.ErrorMessage;
import com.jagitter.data.Message;
import com.jagitter.service.MessageService;
import com.jagitter.service.UserService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@Controller
@RequestMapping("/wall")
@Validated
@Api(value = "wall")
public class WallController {
    private static final Logger logger = LoggerFactory.getLogger(WallController.class);

    private static final int MIN_CHARS = 1;
    private static final int MAX_CHARS = 140;

    private static final String ERR_MESSAGE_LENGTH = "Message shouldn't have more than " +
            MIN_CHARS + " and less than " +
            MAX_CHARS + " characters";


    @Autowired
    private MessageService messageService;

    @Autowired
    private UserService userService;

    @RequestMapping(
            value = "/{user-id}",
            consumes = MediaType.TEXT_PLAIN_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE,
            method = RequestMethod.POST)
    @ResponseStatus(code=HttpStatus.CREATED)
    @ApiOperation(
            value = "Post a new message to user's wall",
            notes = "Post a new message to user's wall.\n\n" +
                    "If user not exist create one.")
    public void newMessage(
            @PathVariable("user-id") String userId,
            @Size(
                min=MIN_CHARS,
                max=MAX_CHARS,
                message = ERR_MESSAGE_LENGTH)
            @ApiParam(value=ERR_MESSAGE_LENGTH)
            @RequestBody String message) {
        logger.info("newMessage");

        if (!userService.userExists(userId)) {
            userService.createUser(userId);
        }

        messageService.postNewMessage(userId, message);
    }


    @RequestMapping(
            value = "/{user-id}",
            params = { "page", "size" },
            consumes = MediaType.ALL_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE,
            method = RequestMethod.GET)
    @ApiOperation(
            value = "Get user's posted messages",
            notes = "Get user's posted messages")
    public ResponseEntity<List<Message>> getMessages(
            @PathVariable("user-id") String userId,
            @RequestParam(
                    name="page",
                    required=false,
                    defaultValue="0") int page,
            @RequestParam(
                    name="size",
                    required=false,
                    defaultValue="10") int size) {
        logger.info("getMessages");

        if (!userService.userExists(userId)) {
            throw new WallControllerException("User '" + userId + "' does not exist");
        }

        List<Message> messages = messageService.getMessages(userId, page, size);

        return ResponseEntity.ok().body(messages);
    }


    private class WallControllerException extends RuntimeException {
        private static final long serialVersionUID = 1L;

        public WallControllerException(String msg) {
            super(msg);
        }
    }


    @ExceptionHandler
    @ResponseBody
    public ResponseEntity<ErrorMessage> handleConstraintViolationException(ConstraintViolationException exception) {
        logger.info("handleConstraintViolationException");

        Optional<ConstraintViolation<?>> firstViolation = exception
                .getConstraintViolations()
                .stream()
                .findFirst();
        String message = firstViolation.isPresent() ?
                firstViolation.get().getMessage() : ErrorMessage.MSG_BAD_REQUEST;

        return ResponseEntity.badRequest().body(new ErrorMessage(message));
    }


    @ExceptionHandler(WallControllerException.class)
    @ResponseBody
    public ResponseEntity<ErrorMessage> handleException(WallControllerException exception) {
        logger.info("handleException");

        return ResponseEntity.badRequest()
                .body(new ErrorMessage(exception.getMessage()));
    }
}
