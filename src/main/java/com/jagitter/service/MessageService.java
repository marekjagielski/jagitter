package com.jagitter.service;

import java.util.List;

import com.jagitter.data.Message;

public interface MessageService {

    void postNewMessage(String userId, String message);

    List<Message> getMessages(String userId, int pageNb, int pageSize);

    List<Message> getMessages(List<String> usersIds, int pageNb, int pageSize);
}
