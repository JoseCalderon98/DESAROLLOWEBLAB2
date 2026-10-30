package com.udea.banktdea.exception;

/**
 * Exception thrown when business rules in banking operations are violated.
 */
public class BankingException extends RuntimeException {

    public BankingException(String message) {
        super(message);
    }
}

