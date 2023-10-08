package org.pragma.restaurantplaza.application.handler;

import org.pragma.restaurantplaza.application.dto.RestaurantRequest;
import org.pragma.restaurantplaza.application.dto.UserRequest;
import org.pragma.restaurantplaza.domain.model.User;

public interface IRestaurantHandler {
    void saveRestaurant(RestaurantRequest restaurantRequest);

}
