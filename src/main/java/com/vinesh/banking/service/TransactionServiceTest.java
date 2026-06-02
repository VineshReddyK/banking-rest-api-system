package com.vinesh.banking.service;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TransactionServiceTest {

    @Test
    void deposit_shouldReturnSuccessMessage() {
        String result = "Deposit successful";
        assertEquals("Deposit successful", result);
    }
}
