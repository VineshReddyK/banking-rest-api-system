package com.vinesh.banking.exception;

// thrown on duplicate email or username during registration —
// the exception handler maps this to a 409 Conflict response.
public class DuplicateResourceException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public DuplicateResourceException(String message) {
        super(message);
    }
}
