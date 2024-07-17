package com.example.Authentication.service.impl;

import com.example.Authentication.entity.UserCredentials;
import com.example.Authentication.payload.LoginDto;
import com.example.Authentication.payload.RegisterDto;
import com.example.Authentication.payload.UpdatePasswordDto;
import com.example.Authentication.repository.UserCredentialsRepository;
import com.example.Authentication.security.JwtTokenProvider;
import com.example.Authentication.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class AuthServiceImplementation implements AuthService {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @Autowired
    UserCredentialsRepository userCredentialsRepository;

    @Autowired
    PasswordEncoder passwordEncoder;


    @Override
    public String Login(LoginDto loginDto) {
        String name = loginDto.getUsernameOrEmail();
        String password = loginDto.getPassword();

        Authentication authentication = authenticationManager.
                authenticate(
                        new UsernamePasswordAuthenticationToken(name, password)
                );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = jwtTokenProvider.generateToken(authentication);
        return token;
    }

    @Override
    public String Register(RegisterDto registerDto) {
        if(userCredentialsRepository.existsByUsername(registerDto.getUsername())){
            return "User with given username already exists";
        }
        if(userCredentialsRepository.existsByEmail(registerDto.getEmail())){
            return "This email id is already registered";
        }
        System.out.println(registerDto.getUsername());
        UserCredentials user = new UserCredentials();
        user.setUsername(registerDto.getUsername());
        user.setEmail(registerDto.getEmail());
        user.setPassword(passwordEncoder.encode(registerDto.getPassword()));

        userCredentialsRepository.save(user);
        return "User registered successfully";
    }

    @Override
    public void validateToken(String token){
        jwtTokenProvider.validateToken(token);
    }

    @Override
    public String updatePassword(UpdatePasswordDto updatePasswordDto) {
        String username = updatePasswordDto.getUsername();
        UserCredentials userCredentials = userCredentialsRepository.findByUsernameOrEmail(username, username).orElseThrow(
                ()->new RuntimeException("User not found"));
        String oldPassword = userCredentials.getPassword();
        String newPassword = updatePasswordDto.getNewPassword();

        // Check if the new password matches any of the old passwords
        for (String oldPwd : userCredentials.getOldPassword()) {
            if (passwordEncoder.matches(newPassword, oldPwd)) {
                return "The new password cannot be the same as any of the old passwords";
            }
        }

        // Encode the new password
        String encodedNewPassword = passwordEncoder.encode(newPassword);

        // Add the current password to the old passwords collection
        userCredentials.getOldPassword().add(oldPassword);

        // Update the password
        userCredentials.setPassword(encodedNewPassword);
        userCredentialsRepository.save(userCredentials);

        return "Password Updated Successfully";
    }
}
