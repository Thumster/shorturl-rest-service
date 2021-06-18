package com.example.shorturlrestservice.exception;

public class UrlInvalidException extends Exception {
    public UrlInvalidException(String errorMessage) {
        super(errorMessage);
    }
}
