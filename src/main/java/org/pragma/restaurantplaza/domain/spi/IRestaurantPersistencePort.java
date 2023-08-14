package org.pragma.restaurantplaza.domain.spi;

import org.pragma.restaurantplaza.domain.model.Owner;
import org.pragma.restaurantplaza.domain.model.Restaurant;

public interface IRestaurantPersistencePort{
    void saveRestaurant(Restaurant restaurant, Owner owner);


}
