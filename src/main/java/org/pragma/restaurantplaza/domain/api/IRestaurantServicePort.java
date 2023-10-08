package org.pragma.restaurantplaza.domain.api;

import org.pragma.restaurantplaza.domain.model.Restaurant;
import org.pragma.restaurantplaza.domain.model.User;

public interface IRestaurantServicePort {
    void saveRestaurant(Restaurant restaurant);
}
