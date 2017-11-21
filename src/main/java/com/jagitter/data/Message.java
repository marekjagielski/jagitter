package com.jagitter.data;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;

public class Message {

    @Id
    private String id;

    private Date createDate = new Date();

    @Indexed
    private String userId;

    private String text;

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }


    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Message() {}

    public Message(String userId, String text) {
        this.userId = userId;
        this.text = text;
    }
}
