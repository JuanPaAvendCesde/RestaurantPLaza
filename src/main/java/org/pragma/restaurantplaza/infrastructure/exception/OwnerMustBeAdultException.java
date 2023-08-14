package org.pragma.restaurantplaza.infrastructure.exception;

public class OwnerMustBeAdultException extends RuntimeException{
    public OwnerMustBeAdultException(String message) {
        super(message);
    }
}
