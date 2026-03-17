package com.youssef.auth_app.service;

import com.youssef.auth_app.model.Otp;
import com.youssef.auth_app.repository.OtpRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;

@Service
public class OtpService {
    @Autowired
    private OtpRepository otpRepository;

    @Autowired
    private EmailService emailService;

    public void generatedAndSendOtp(String email){
        String code = String.format("%06d", new Random().nextInt(999999));

        Otp otp = new Otp();
        otp.setEmail(email);
        otp.setOtpCode(code);
        otp.setExpiresAt(LocalDateTime.now().plusMinutes(10));
        otp.setUsed(false);
        otpRepository.save(otp);
        emailService.sendOtpEmail(email,code);
    }
    public boolean verifyOtp(String email, String code){
        Otp otp = otpRepository.findTopByEmailOrderByExpiresAtDesc(email)
                .orElseThrow(()-> new RuntimeException("OTP not found"));
        if (otp.getExpiresAt().isBefore(LocalDateTime.now())){
            throw new RuntimeException("OTP has expired");
        }
        if (otp.isUsed()){
            throw new RuntimeException("OTP already used");
        }
        if (!otp.getOtpCode().equals(code)){
            throw new RuntimeException("Invalid OTP");
        }
        otp.setUsed(true);
        otpRepository.save(otp);
        return true;
    }
}
