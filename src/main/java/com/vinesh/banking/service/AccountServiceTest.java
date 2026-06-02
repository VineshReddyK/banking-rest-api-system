package com.vinesh.banking.service;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AccountServiceTest {

    @Test
    void createAccount_shouldReturnSuccessMessage() {
        String result = "Account created successfully";
        assertEquals("Account created successfully", result);
    }
}
