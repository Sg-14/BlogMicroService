package com.example.Authentication.service;

import com.example.Authentication.payload.LoginDto;
import com.example.Authentication.payload.RegisterDto;

public interface AuthService {
    String Login(LoginDto loginDto);
    String Register(RegisterDto registerDto);
    void validateToken(String token);
}
