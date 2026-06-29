package com.vinesh.banking.service;

import com.vinesh.banking.dto.LoginRequest;
import com.vinesh.banking.dto.LoginResponse;
import com.vinesh.banking.dto.RegisterRequest;
import com.vinesh.banking.entity.User;
import com.vinesh.banking.exception.DuplicateResourceException;
import com.vinesh.banking.exception.ResourceNotFoundException;
import com.vinesh.banking.repository.UserRepository;
import com.vinesh.banking.security.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private final AuditLogService auditLogService;
    private final EmailNotificationService emailNotificationService;

    public UserService(UserRepository userRepository,
                       JwtUtil jwtUtil,
                       PasswordEncoder passwordEncoder,
                       AuditLogService auditLogService,
                       EmailNotificationService emailNotificationService) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
        this.auditLogService = auditLogService;
        this.emailNotificationService = emailNotificationService;
    }

    public String registerUser(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("Email already in use: " + request.getEmail());
        }

        // username uniqueness check — keeping this separate from email so we can give a specific message
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new DuplicateResourceException("Username already taken: " + request.getUsername());
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        userRepository.save(user);

        auditLogService.log("USER_REGISTER", request.getEmail(), "New user registered: " + request.getEmail());
        emailNotificationService.sendWelcomeEmail(request.getEmail(), request.getUsername());
        log.info("User registered successfully: {}", request.getEmail());

        return "User registered successfully";
    }

    public LoginResponse loginUser(LoginRequest request) {
        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("No account found for email: " + request.getEmail()));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Invalid credentials");
        }

        String token = jwtUtil.generateToken(user.getEmail());
        auditLogService.log("USER_LOGIN", user.getEmail(), "User logged in: " + user.getEmail());

        LoginResponse response = new LoginResponse();
        response.setToken(token);
        return response;
    }
}
