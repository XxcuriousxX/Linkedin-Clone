package com.example.tedi_app.exceptions;

public class SpringTediException extends RuntimeException{
    public SpringTediException(String exMessage, Exception exception) {
        super(exMessage, exception);
    }

    public SpringTediException(String exMessage) {
        super(exMessage);
    }
}
