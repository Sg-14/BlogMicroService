package com.example.Authentication.payload;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdatePasswordDto {
    private String username;
    private String newPassword;
}
