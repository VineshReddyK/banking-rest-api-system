package com.vinesh.banking.service;

import com.vinesh.banking.dto.LoginRequest;
import com.vinesh.banking.dto.LoginResponse;
import com.vinesh.banking.dto.RegisterRequest;
import com.vinesh.banking.entity.User;
import com.vinesh.banking.exception.DuplicateResourceException;
import com.vinesh.banking.exception.ResourceNotFoundException;
import com.vinesh.banking.repository.UserRepository;
import com.vinesh.banking.security.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private final AuditLogService auditLogService;
    private final EmailNotificationService emailService;

    public UserService(UserRepository userRepository, JwtUtil jwtUtil,
                       PasswordEncoder passwordEncoder, AuditLogService auditLogService,
                       EmailNotificationService emailService) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
        this.auditLogService = auditLogService;
        this.emailService = emailService;
    }

    public String registerUser(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("Email already in use: " + request.getEmail());
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        userRepository.save(user);

        auditLogService.log("USER_REGISTER", request.getEmail(), "New user registered: " + request.getEmail());
        emailService.sendWelcomeEmail(request.getEmail(), request.getUsername());

        return "User registered successfully";
    }

    public LoginResponse loginUser(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + request.getEmail()));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Invalid credentials");
        }

        auditLogService.log("USER_LOGIN", user.getEmail(), "User logged in: " + user.getEmail());

        LoginResponse response = new LoginResponse();
        response.setToken(jwtUtil.generateToken(user.getEmail()));
        return response;
    }
}
