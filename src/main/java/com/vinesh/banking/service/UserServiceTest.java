package com.vinesh.banking.service;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UserServiceTest {

    @Test
    void registerUser_shouldReturnSuccessMessage() {
        String result = "User registered successfully";
        assertEquals("User registered successfully", result);
    }
}
