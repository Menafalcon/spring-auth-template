package com.youssef.auth_app.repository;

import com.youssef.auth_app.model.Otp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface OtpRepository extends JpaRepository<Otp, Long> {
    Optional<Otp> findTopByEmailOrderByExpiresAtDesc(String email);
}
