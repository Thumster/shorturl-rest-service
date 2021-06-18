package com.example.shorturlrestservice.exception;

public class UrlNotFoundException extends Exception {
    public UrlNotFoundException(String errorMessage) {
        super(errorMessage);
    }
}
