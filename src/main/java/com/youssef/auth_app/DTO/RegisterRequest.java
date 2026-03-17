package com.youssef.auth_app.DTO;

import lombok.Data;

@Data
public class RegisterRequest {
    private String userName;
    private String userPassword;
    private String userEmail;
    private String userTel;
}
