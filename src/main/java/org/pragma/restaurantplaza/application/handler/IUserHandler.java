package org.pragma.restaurantplaza.application.handler;

import org.pragma.restaurantplaza.application.dto.UserRequest;
import org.pragma.restaurantplaza.domain.model.User;

public interface IUserHandler {

    void saveUser(UserRequest userRequest);

    User findById(User userId);
}
