package com.example.quoteservice.controller;

import com.example.quoteservice.model.exceptions.ErrorResponse;
import com.example.quoteservice.model.exceptions.NoSuchEntityException;
import com.example.quoteservice.model.exceptions.UserDataBaseException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.NoSuchElementException;

@RestControllerAdvice
public class ControllerAdvisor extends ResponseEntityExceptionHandler {

    @ExceptionHandler(NoSuchElementException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNoSuchElementException(NoSuchElementException noSuchElementException) {
        return ErrorResponse.builder()
                .message(noSuchElementException.getMessage())
                .httpStatus(HttpStatus.NOT_FOUND)
                .build();
    }

    @ExceptionHandler(NoSuchEntityException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNoSuchEntityException(NoSuchEntityException noSuchEntityException) {
        return ErrorResponse.builder()
                .message(noSuchEntityException.getMessage())
                .httpStatus(HttpStatus.NOT_FOUND)
                .build();
    }
    @ExceptionHandler(IllegalAccessError.class)
    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    public ErrorResponse handleIllegalAccessError(IllegalAccessError illegalAccessError) {
        return ErrorResponse.builder()
                .message(illegalAccessError.getMessage())
                .httpStatus(HttpStatus.NOT_ACCEPTABLE)
                .build();
    }

    @ExceptionHandler(UserDataBaseException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleUserDataBaseException(UserDataBaseException userDataBaseException) {
        return ErrorResponse.builder()
                .message(userDataBaseException.getMessage())
                .httpStatus(HttpStatus.NOT_FOUND)
                .build();
    }






}
