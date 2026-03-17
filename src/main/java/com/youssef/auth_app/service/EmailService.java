package com.youssef.auth_app.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
public class EmailService {

    @Value("${brevo.api.key}")
    private String apiKey;

    @Value("${mail.from}")
    private String fromEmail;

    private static final String BREVO_API_URL = "https://api.brevo.com/v3/smtp/email";

    private void sendEmail(String toEmail, String subject, String text) {
        try {
            String body = String.format("""
                {
                    "sender": {"email": "%s"},
                    "to": [{"email": "%s"}],
                    "subject": "%s",
                    "textContent": "%s"
                }
                """, fromEmail, toEmail, subject, text.replace("\n", "\\n").replace("\"", "\\\""));

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BREVO_API_URL))
                    .header("accept", "application/json")
                    .header("api-key", apiKey)
                    .header("content-type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(body))
                    .build();

            HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            throw new RuntimeException("Failed to send email: " + e.getMessage());
        }
    }

    public void sendOtpEmail(String toEmail, String otp) {
        sendEmail(toEmail, "Your OTP Code",
                "Your OTP code is: " + otp + "\n\nThis code expires in 10 minutes.");
    }

    public void sendPasswordResetEmail(String toEmail, String resetLink) {
        sendEmail(toEmail, "Password Reset Request",
                "Click the link below to reset your password:\n\n" + resetLink +
                        "\n\nThis link expires in 30 minutes.\n\nIf you didn't request this, ignore this email.");
    }
}