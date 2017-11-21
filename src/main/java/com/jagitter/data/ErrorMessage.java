package com.jagitter.data;

public class ErrorMessage {

    public static final String MSG_BAD_REQUEST = "Bad request";

    private String message;

    public ErrorMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
