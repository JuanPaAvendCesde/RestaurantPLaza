package org.pragma.restaurantplaza.infrastructure.exception;

public class InvalidNitFormatException extends RuntimeException {
    public InvalidNitFormatException(String message) {
        super(message);
    }
}
