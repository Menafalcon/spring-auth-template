package com.youssef.auth_app.controller;

import com.youssef.auth_app.DTO.AuthResponse;
import com.youssef.auth_app.DTO.LoginRequest;
import com.youssef.auth_app.DTO.RegisterRequest;
import com.youssef.auth_app.service.AuthService;
import com.youssef.auth_app.service.PasswordResetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private AuthService authService;
    @Autowired
    private PasswordResetService passwordResetService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest request){
        String response = authService.register(request);
        return ResponseEntity.ok(response);
    }
    @PostMapping("/verify-otp")
    public ResponseEntity<AuthResponse> verifyOtp(
            @RequestParam String email,
            @RequestParam String code){
        AuthResponse response = authService.verifyOtp(email, code);
        return ResponseEntity.ok(response);
    }
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request){
        AuthResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }
    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestParam String email) {
        String response = passwordResetService.requestPasswordReset(email);
        return ResponseEntity.ok(response);
    }
    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(
            @RequestParam String token,
            @RequestParam String newPassword) {
        String response = passwordResetService.resetPassword(token, newPassword);
        return ResponseEntity.ok(response);
    }
}







