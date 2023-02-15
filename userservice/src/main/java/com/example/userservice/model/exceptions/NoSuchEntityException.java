package com.example.userservice.model.exceptions;

public class NoSuchEntityException extends RuntimeException{
    public NoSuchEntityException(String message) {
        super(message);
    }
}
