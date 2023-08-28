package org.pragma.restaurantplaza.infrastructure.exception;

public class UserMustBeClientException extends RuntimeException{
    public UserMustBeClientException(String message) {
        super(message);
    }
}
