package com.youssef.auth_app.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserProfileResponse {
    private String userName;
    private String userEmail;
    private String userTel;
}
