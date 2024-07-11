package com.example.API_Gatway.exception;


import lombok.Data;

@Data
public class ExceptionEntity {
    private String message;
    private int errorCode;
}
