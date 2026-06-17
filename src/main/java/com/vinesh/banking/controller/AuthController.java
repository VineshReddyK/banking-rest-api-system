package com.vinesh.banking.controller;

import com.vinesh.banking.dto.LoginRequest;
import com.vinesh.banking.dto.LoginResponse;
import com.vinesh.banking.dto.RegisterRequest;
import com.vinesh.banking.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest request) {
        String message = userService.registerUser(request);
        return new ResponseEntity<>(message, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        LoginResponse response = userService.loginUser(request);
        return ResponseEntity.ok(response);
    }
}
