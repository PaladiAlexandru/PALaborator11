package com.main.exceptions;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

import org.springframework.web.bind.annotation.RestControllerAdvice;


import java.time.LocalDateTime;

@RestControllerAdvice
public class MyExceptionAdvice {
    @ExceptionHandler(value = MyException.class)
    public ResponseEntity<ErrorMessage>
    handleGenericNotFoundException(MyException e) {
        ErrorMessage error = new ErrorMessage(e.getMessage());
        error.setTimestamp(LocalDateTime.now());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }
}