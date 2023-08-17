package org.pragma.restaurantplaza.application.handler;

import org.pragma.restaurantplaza.application.dto.UserRequest;
import org.pragma.restaurantplaza.application.dto.UserResponse;
import org.pragma.restaurantplaza.domain.model.User;

import java.util.List;

public interface IUserHandler {

    void saveUser(UserRequest userRequest);
    List<UserResponse> getAllOwners();
    User findById(User userId);
}
