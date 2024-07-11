package com.example.API_Gatway.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionEntity> genericException(Exception ex){
        String message = ex.getMessage();
        int errorCode = HttpStatus.UNAUTHORIZED.value();

        ExceptionEntity exceptionEntity = new ExceptionEntity();
        exceptionEntity.setMessage(message);
        exceptionEntity.setErrorCode(errorCode);
        return new ResponseEntity<>(exceptionEntity, HttpStatus.UNAUTHORIZED);
    }
}
