package org.pragma.restaurantplaza.infrastructure.exception;

public class InvalidMealPriceException extends RuntimeException {
    public InvalidMealPriceException(String message) {
        super(message);
    }
}
