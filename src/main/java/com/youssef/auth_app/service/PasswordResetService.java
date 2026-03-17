package com.youssef.auth_app.service;

import com.youssef.auth_app.model.PasswordResetToken;
import com.youssef.auth_app.model.User;
import com.youssef.auth_app.repository.PasswordResetTokenRepository;
import com.youssef.auth_app.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class PasswordResetService {
    @Autowired
    private PasswordResetTokenRepository tokenRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private EmailService emailService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public String requestPasswordReset(String email){
        userRepository.findByUserEmail(email)
                .orElseThrow(()-> new RuntimeException("No Account was found with this email"));
        String token = UUID.randomUUID().toString();
        PasswordResetToken resetToken = new PasswordResetToken();
        resetToken.setEmail(email);
        resetToken.setToken(token);
        resetToken.setExpiresAt(LocalDateTime.now().plusMinutes(30));
        resetToken.setUsed(false);
        tokenRepository.save(resetToken);

        String resetLink = "http://localhost:5173/reset-password?token=" + token;
        emailService.sendPasswordResetEmail(email, resetLink);
        return "Password reset link sent to your email";
    }

    @Transactional
    public String resetPassword(String token, String newPassword){
        PasswordResetToken resetToken = tokenRepository.findByToken(token)
                .orElseThrow(()-> new RuntimeException("Invalid Reset Link"));
        if (resetToken.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Reset Link has expired");
        }
        if (resetToken.isUsed()){
            throw new RuntimeException("Reset Link already used");
        }
        User user = userRepository.findByUserEmail(resetToken.getEmail())
                .orElseThrow(()-> new RuntimeException("User not found"));
        user.setUserPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        resetToken.setUsed(true);
        tokenRepository.save(resetToken);
        return "Password reset Successfully!";
    }

}










