package com.example.Authentication.controller;

import com.example.Authentication.payload.AuthResponse;
import com.example.Authentication.payload.LoginDto;
import com.example.Authentication.payload.RegisterDto;
import com.example.Authentication.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginDto loginDto){
        String response = authService.Login(loginDto);
        AuthResponse authResponse = new AuthResponse();
        authResponse.setToken(response);
        return ResponseEntity.ok(authResponse.getToken());
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterDto registerDto){
        String response = authService.Register(registerDto);
        if(!response.contains("successfully")){
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/validate")
    public ResponseEntity<String> validate(@RequestParam("token") String token){
        authService.validateToken(token);
        return ResponseEntity.ok("Token is valid");
    }
}