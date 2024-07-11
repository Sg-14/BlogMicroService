package com.example.Authentication.payload;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthResponse {
    private String token;
    private String bearer="Bearer";
}
