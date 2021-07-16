package com.example.tedi_app.exception;

public class LinkedInException extends RuntimeException {
    public LinkedInException(String exMessage, Exception exception) {
        super(exMessage, exception);
    }

    public LinkedInException(String exMessage) {
        super(exMessage);
    }
}

