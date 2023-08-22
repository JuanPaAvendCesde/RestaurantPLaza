package org.pragma.restaurantplaza.infrastructure.exception;

public class UserMustBeAdultException extends RuntimeException {
    public UserMustBeAdultException(String message) {
        super(message);
    }
}
