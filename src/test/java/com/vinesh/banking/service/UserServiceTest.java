package com.vinesh.banking.service;

import com.vinesh.banking.dto.LoginRequest;
import com.vinesh.banking.dto.LoginResponse;
import com.vinesh.banking.dto.RegisterRequest;
import com.vinesh.banking.entity.User;
import com.vinesh.banking.exception.DuplicateResourceException;
import com.vinesh.banking.exception.ResourceNotFoundException;
import com.vinesh.banking.repository.UserRepository;
import com.vinesh.banking.security.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuditLogService auditLogService;

    @Mock
    private EmailNotificationService emailNotificationService;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void registerUser_newEmail_shouldReturnSuccessMessage() {
        when(userRepository.existsByEmail("test@example.com")).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("$2a$10$encodedpassword");
        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArgument(0));

        RegisterRequest request = new RegisterRequest();
        request.setUsername("vinesh");
        request.setEmail("test@example.com");
        request.setPassword("pass123");

        String result = userService.registerUser(request);

        assertEquals("User registered successfully", result);
    }

    @Test
    void registerUser_duplicateEmail_shouldThrowDuplicateResourceException() {
        when(userRepository.existsByEmail("test@example.com")).thenReturn(true);

        RegisterRequest request = new RegisterRequest();
        request.setEmail("test@example.com");

        assertThrows(DuplicateResourceException.class, () -> userService.registerUser(request));
    }

    @Test
    void loginUser_validCredentials_shouldReturnJwtToken() {
        User user = new User();
        user.setEmail("test@example.com");
        user.setPassword("$2a$10$encodedpassword");

        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("pass123", "$2a$10$encodedpassword")).thenReturn(true);
        when(jwtUtil.generateToken("test@example.com")).thenReturn("mock-jwt-token");

        LoginRequest request = new LoginRequest();
        request.setEmail("test@example.com");
        request.setPassword("pass123");

        LoginResponse response = userService.loginUser(request);

        assertEquals("mock-jwt-token", response.getToken());
    }

    @Test
    void loginUser_wrongPassword_shouldThrowIllegalArgumentException() {
        User user = new User();
        user.setEmail("test@example.com");
        user.setPassword("$2a$10$encodedpassword");

        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrongpass", "$2a$10$encodedpassword")).thenReturn(false);

        LoginRequest request = new LoginRequest();
        request.setEmail("test@example.com");
        request.setPassword("wrongpass");

        assertThrows(IllegalArgumentException.class, () -> userService.loginUser(request));
    }

    @Test
    void loginUser_emailNotRegistered_shouldThrowResourceNotFoundException() {
        when(userRepository.findByEmail("ghost@example.com")).thenReturn(Optional.empty());

        LoginRequest request = new LoginRequest();
        request.setEmail("ghost@example.com");
        request.setPassword("pass");

        assertThrows(ResourceNotFoundException.class, () -> userService.loginUser(request));
    }
}
