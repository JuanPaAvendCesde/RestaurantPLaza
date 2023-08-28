package org.pragma.restaurantplaza.domain.api;

import org.pragma.restaurantplaza.domain.model.Restaurant;
import org.pragma.restaurantplaza.domain.model.User;

public interface IUserServicePort {
    void saveUser(User user);

    User findById(User userId);

    Restaurant getEmployeeRestaurant(Long id);
}
