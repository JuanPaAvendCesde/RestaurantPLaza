package org.pragma.restaurantplaza.domain.spi;

import org.pragma.restaurantplaza.application.dto.MealResponse;
import org.pragma.restaurantplaza.domain.model.Restaurant;
import org.pragma.restaurantplaza.domain.model.User;
import org.springframework.data.domain.Page;



public interface IRestaurantPersistencePort {
    void saveRestaurant(Restaurant restaurant, User user);
    Page<MealResponse> getRestaurantMenuByCategory(Long restaurantId, String name, String category, int page, int size);


}
