package com.youssef.auth_app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    public void sendOtpEmail(String toEmail, String otp){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(toEmail);
        message.setSubject("Your OTP Code");
        message.setText("Your OTP code is: "+otp+ "\n\nThis code expires in 10 minutes.");
        mailSender.send(message);
    }
    public void sendPasswordResetEmail(String toEmail, String resetLink){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(toEmail);
        message.setSubject("Password Reset Request");
        message.setText("Click the link below to reset your password:\n\n" + resetLink + "\n\nThis link expires in 30 minutes.\n\nIf you didn't request this, ignore this email.");
        mailSender.send(message);
    }
}
