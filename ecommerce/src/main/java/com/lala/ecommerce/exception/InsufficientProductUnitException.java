package com.lala.ecommerce.exception;

public class InsufficientProductUnitException extends RuntimeException{
    public InsufficientProductUnitException(String message) {
        super(message);
    }
}
