package com.jagitter.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jagitter.data.Message;
import com.jagitter.data.User;
import com.jagitter.service.MessageService;
import com.jagitter.service.UserService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/timeline")
@Api(value = "timeline")
public class TimelineController {

    @Autowired
    private MessageService messageService;

    @Autowired
    private UserService userService;

    @RequestMapping(
            value = "/{user-id}",
            params = { "page", "size" },
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE,
            method = RequestMethod.GET)
    @ApiOperation(
            value = "Get timeline",
            notes = "Timeline is a set of messages of followed users")
    public ResponseEntity<List<Message>> getTimeline(
            @PathVariable("user-id") String userId,
            @RequestParam(
                    name="page",
                    required=false,
                    defaultValue="0") int page,
            @RequestParam(
                    name="size",
                    required=false,
                    defaultValue="10") int size) {

        List<User> users = userService.getFollowedUsers(userId);
        List<String> usersIds = users.stream().map(User::getUserId).collect(Collectors.toList());
        List<Message> messages = messageService.getMessages(usersIds, page, size);
        return ResponseEntity.ok().body(messages);
    }
}
