package com.example.Authentication.service;

import com.example.Authentication.payload.LoginDto;
import com.example.Authentication.payload.RegisterDto;
import com.example.Authentication.payload.UpdatePasswordDto;
import jakarta.servlet.http.HttpServletRequest;

public interface AuthService {
    String Login(LoginDto loginDto);
    String Register(RegisterDto registerDto);
    void validateToken(String token);
    String updatePassword(UpdatePasswordDto updatePasswordDto);

}
