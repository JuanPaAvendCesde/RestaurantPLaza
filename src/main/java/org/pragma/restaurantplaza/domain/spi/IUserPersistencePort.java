package org.pragma.restaurantplaza.domain.spi;

import org.pragma.restaurantplaza.domain.model.Restaurant;
import org.pragma.restaurantplaza.domain.model.User;

import java.util.List;

public interface IUserPersistencePort {
    void saveUser(User user);
    List<User> getAllOwners();
    User findById(User userId);

    Restaurant getEmployeeRestaurant(Long id);
}
