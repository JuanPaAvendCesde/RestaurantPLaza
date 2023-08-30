package org.pragma.restaurantplaza.infrastructure.exception;

public class InvalidStateException extends RuntimeException{
    public InvalidStateException(String message) {
        super(message);
    }
}
