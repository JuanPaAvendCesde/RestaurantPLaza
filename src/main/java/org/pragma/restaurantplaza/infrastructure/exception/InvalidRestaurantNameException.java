package org.pragma.restaurantplaza.infrastructure.exception;

public class InvalidRestaurantNameException extends RuntimeException {
    public InvalidRestaurantNameException(String message) {
        super(message);
    }
}
