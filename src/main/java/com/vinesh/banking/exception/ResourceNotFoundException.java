package com.vinesh.banking.exception;

// thrown when a lookup by id or email comes back empty —
// the exception handler maps this to a 404 response.
public class ResourceNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public ResourceNotFoundException(String message) {
        super(message);
    }
}
