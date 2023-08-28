package org.pragma.restaurantplaza.application.handler;

import lombok.RequiredArgsConstructor;
import org.pragma.restaurantplaza.application.dto.UserRequest;
import org.pragma.restaurantplaza.application.mapper.UserRequestMapper;
import org.pragma.restaurantplaza.domain.api.IUserServicePort;
import org.pragma.restaurantplaza.domain.model.Restaurant;
import org.pragma.restaurantplaza.domain.model.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserHandler implements IUserHandler {

    private final IUserServicePort userServicePort;
    private final UserRequestMapper userRequestMapper;

    @Override
    public void saveUser(UserRequest userRequest) {
        User user = userRequestMapper.toOwner(userRequest);
        userServicePort.saveUser(user);
    }

    @Override
    public User findById(User userId) {
        return userServicePort.findById(userId);
    }


    public Restaurant getEmployeeRestaurant(Long id) {
        return userServicePort.getEmployeeRestaurant(id);
    }
}
