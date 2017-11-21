package com.jagitter.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.jagitter.data.Message;
import com.jagitter.repository.MessageRepository;

@Service("messageService")
public class MessageServiceImpl implements MessageService {

    private static final Logger logger = LoggerFactory.getLogger(MessageServiceImpl.class);

    @Autowired
    private MessageRepository repository;


    @Override
    public void postNewMessage(String userId, String text) {
        Message message = new Message(userId, text);
        repository.save(message);
    }


    @Override
    public List<Message> getMessages(String userId, int pageNb, int pageSize) {
        logger.trace("getMessages({}, {}, {})", userId, pageNb, pageSize);

        Pageable pageable = new PageRequest(pageNb, pageSize, Direction.DESC, "createDate");
        Page<Message> page = repository.findByUserId(userId, pageable);
        return Lists.newArrayList(page);
    }


    @Override
    public List<Message> getMessages(List<String> usersIds, int pageNb, int pageSize) {
        Pageable pageable = new PageRequest(pageNb, pageSize, Direction.DESC, "createDate");
        Page<Message> page = repository.findByUserIds(usersIds.toArray(new String[usersIds.size()]), pageable);

        return Lists.newArrayList(page);
    }
}
