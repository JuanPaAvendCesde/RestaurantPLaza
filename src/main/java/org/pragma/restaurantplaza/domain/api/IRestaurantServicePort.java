package org.pragma.restaurantplaza.domain.api;

import org.pragma.restaurantplaza.domain.model.User;
import org.pragma.restaurantplaza.domain.model.Restaurant;

public interface IRestaurantServicePort {
    void saveRestaurant(Restaurant restaurant, User user);

}
