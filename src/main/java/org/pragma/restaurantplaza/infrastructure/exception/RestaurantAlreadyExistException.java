package org.pragma.restaurantplaza.infrastructure.exception;

public class RestaurantAlreadyExistException extends RuntimeException {

    public RestaurantAlreadyExistException(String message) {
        super(message);
    }
}
