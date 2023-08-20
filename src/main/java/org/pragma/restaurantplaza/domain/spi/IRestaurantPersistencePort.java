package org.pragma.restaurantplaza.domain.spi;

import org.pragma.restaurantplaza.domain.model.User;
import org.pragma.restaurantplaza.domain.model.Restaurant;

public interface IRestaurantPersistencePort{
    void saveRestaurant(Restaurant restaurant, User user);


}
