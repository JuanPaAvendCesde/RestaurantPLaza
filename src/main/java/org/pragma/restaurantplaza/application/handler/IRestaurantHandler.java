package org.pragma.restaurantplaza.application.handler;

import org.pragma.restaurantplaza.application.dto.RestaurantRequest;
import org.pragma.restaurantplaza.application.dto.UserRequest;

public interface IRestaurantHandler {
    void saveRestaurant(RestaurantRequest restaurantRequest, UserRequest userRequest);

}
