package com.youssef.auth_app.DTO;

import lombok.Data;

@Data
public class LoginRequest {
    private String userName;
    private String userPassword;
}
