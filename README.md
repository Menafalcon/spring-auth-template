# Spring Boot Authentication Template

A production-ready authentication system built with Spring Boot 4, featuring JWT tokens, BCrypt encryption, OTP email verification, and password recovery.

## Features

- User registration with BCrypt password encryption
- Email OTP verification (10 minute expiry)
- JWT authentication (24 hour session)
- Password recovery via email reset link (30 minute expiry)
- PostgreSQL database
- Spring Security with stateless session management

## Tech Stack

- Java 21
- Spring Boot 4.0.3
- Spring Security 7
- Spring Data JPA / Hibernate
- PostgreSQL
- JWT (jjwt 0.12.6)
- JavaMailSender (Gmail SMTP)
- Lombok

## Project Structure
```
src/main/java/com/youssef/auth_app/
│
├── config/
│   ├── JwtUtil.java           # JWT generation and validation
│   ├── JwtAuthFilter.java     # JWT filter for protected routes
│   └── SecurityConfig.java    # Spring Security configuration
│
├── controller/
│   └── AuthController.java    # Authentication endpoints
│
├── dto/
│   ├── LoginRequest.java
│   ├── RegisterRequest.java
│   └── AuthResponse.java
│
├── model/
│   ├── User.java
│   ├── Otp.java
│   └── PasswordResetToken.java
│
├── repository/
│   ├── UserRepository.java
│   ├── OtpRepository.java
│   └── PasswordResetTokenRepository.java
│
└── service/
    ├── AuthService.java
    ├── EmailService.java
    ├── OtpService.java
    └── PasswordResetService.java
```

## Setup

### 1. Prerequisites
- Java 21
- PostgreSQL
- Gmail account with App Password

### 2. Database
Create a PostgreSQL database:
```sql
CREATE DATABASE your_db_name;
```

### 3. Configuration
Create `src/main/resources/application.properties`:
```properties
# Server
server.port=8090

# PostgreSQL
spring.datasource.url=jdbc:postgresql://localhost:5432/your_db_name
spring.datasource.username=postgres
spring.datasource.password=YOUR_POSTGRES_PASSWORD

# JPA
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect

# JWT
jwt.secret=mysecretkeymysecretkeymysecretkeymysecretkey
jwt.expiration=86400000

# Email
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=YOUR_GMAIL@gmail.com
spring.mail.password=YOUR_APP_PASSWORD
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true
```

### 4. Run
```
./mvnw spring-boot:run
```

## API Endpoints

### Public Routes (no token required)

| Method | URL | Body | Description |
|--------|-----|------|-------------|
| POST | `/api/auth/register` | JSON | Register new user |
| POST | `/api/auth/verify-otp` | Params | Verify OTP code |
| POST | `/api/auth/login` | JSON | Login |
| POST | `/api/auth/forgot-password` | Params | Request reset link |
| POST | `/api/auth/reset-password` | Params | Reset password |

### Protected Routes (token required)

Add this header to every request:
```
Authorization: Bearer <your_jwt_token>
```

### Request Examples

**Register:**
```json
POST /api/auth/register
{
    "userName": "john",
    "userPassword": "password123",
    "userEmail": "john@gmail.com",
    "userTel": "12345678"
}
```

**Verify OTP:**
```
POST /api/auth/verify-otp?email=john@gmail.com&code=123456
```

**Login:**
```json
POST /api/auth/login
{
    "userName": "john",
    "userPassword": "password123"
}
```

**Forgot Password:**
```
POST /api/auth/forgot-password?email=john@gmail.com
```

**Reset Password:**
```
POST /api/auth/reset-password?token=UUID_TOKEN&newPassword=newpassword123
```

## Authentication Flow
```
Register → OTP sent to email → Verify OTP → Account activated
Login → JWT token returned → Use token for protected routes
Forgot Password → Reset link sent → Click link → Enter new password
```

## Security Notes

- Passwords are hashed using BCrypt
- JWT tokens expire after 24 hours
- OTP codes expire after 10 minutes
- Reset links expire after 30 minutes
- OTP codes and reset tokens are single use only
- application.properties is excluded from version control

## License

MIT
