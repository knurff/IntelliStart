package com.example.intellistart.web.exceptions;

public class InsufficientUserBalanceException extends RuntimeException {
    public InsufficientUserBalanceException(Long userId, Long productId) {
        super("User with id  " + userId + " could not buy product with id " + productId);
    }
}
