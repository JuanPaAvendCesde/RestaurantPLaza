package org.pragma.restaurantplaza.domain.api;

import org.pragma.restaurantplaza.domain.model.User;

import java.util.List;

public interface IUserServicePort {

    void saveUser(User user);

    List<User> getAllOwners();

    User findById(User userId);
}
