package com.youssef.auth_app.service;

import com.youssef.auth_app.DTO.AuthResponse;
import com.youssef.auth_app.DTO.LoginRequest;
import com.youssef.auth_app.DTO.RegisterRequest;
import com.youssef.auth_app.config.JwtUtil;
import com.youssef.auth_app.model.User;
import com.youssef.auth_app.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private OtpService otpService;

    public String register(RegisterRequest request){
        if(userRepository.findByUserName(request.getUserName()).isPresent()){
            throw new RuntimeException("Username already exists");
        }
        if (userRepository.findByUserEmail(request.getUserEmail()).isPresent()){
            throw new RuntimeException("Email already exists");
        }
        User user = new User();
        user.setUserName(request.getUserName());
        user.setUserPassword(passwordEncoder.encode(request.getUserPassword()));
        user.setUserEmail(request.getUserEmail());
        user.setUserTel(request.getUserTel());
        user.setVerified(false);
        userRepository.save(user);
        otpService.generatedAndSendOtp(request.getUserEmail());
        return "Registration successful! Check your email for the OTP code.";
    }
    public AuthResponse verifyOtp(String email, String code) {
        otpService.verifyOtp(email, code);

        // mark user as verified
        User user = userRepository.findByUserEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setVerified(true);
        userRepository.save(user);

        // now generate token — user is fully logged in
        String token = jwtUtil.generateToken(user.getUserName());
        return new AuthResponse(token);
    }

    public AuthResponse login(LoginRequest request) {
        // check if user is verified
        User user = userRepository.findByUserName(request.getUserName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!user.isVerified()) {
            throw new RuntimeException("Please verify your email first");
        }

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUserName(),
                        request.getUserPassword()
                )
        );

        String token = jwtUtil.generateToken(request.getUserName());
        return new AuthResponse(token);
    }
}
