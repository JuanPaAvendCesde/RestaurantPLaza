package org.pragma.restaurantplaza.infrastructure.exception;

public class UserAlreadyExistException extends RuntimeException{
    public UserAlreadyExistException(String message) {
        super(message);
    }



}
