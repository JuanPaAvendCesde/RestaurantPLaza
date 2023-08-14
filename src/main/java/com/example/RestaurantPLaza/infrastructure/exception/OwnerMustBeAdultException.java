package com.example.RestaurantPLaza.infrastructure.exception;

public class OwnerMustBeAdultException extends RuntimeException{
    public OwnerMustBeAdultException(String message) {
        super(message);
    }
}
