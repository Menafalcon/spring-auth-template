package com.youssef.auth_app.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userID;
    @Column(unique = true, nullable = false)
    private String userName;
    @Column(nullable = false)
    private String userPassword;
    @Column(unique = true, nullable = false)
    private String userEmail;
    private String userTel;
    @Column(nullable = false, columnDefinition = "boolean default false")
    private boolean verified = false;
}
