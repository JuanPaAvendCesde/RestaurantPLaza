package org.pragma.restaurantplaza.application.handler;

import lombok.RequiredArgsConstructor;
import org.pragma.restaurantplaza.application.dto.MealRequest;
import org.pragma.restaurantplaza.application.dto.UserRequest;
import org.pragma.restaurantplaza.application.dto.UserResponse;
import org.pragma.restaurantplaza.application.mapper.UserRequestMapper;
import org.pragma.restaurantplaza.domain.api.IUserServicePort;
import org.pragma.restaurantplaza.domain.model.Restaurant;
import org.pragma.restaurantplaza.domain.model.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

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

    public UserResponse getUserById(Long id) {
        User user = userServicePort.getUserById(id);

        return userRequestMapper.toUserResponse(user);
    }
    public boolean deleteUserById(Long id) {
        boolean deleted = userServicePort.deleteUserById(id);
        return deleted;
    }

    public List<UserResponse> getAllUsers() {
        List<User> users = userServicePort.getAllUsers(); // Supongamos que tienes un método en userServicePort para obtener todos los usuarios
        return users.stream()
                .map(userRequestMapper::toUserResponse)
                .collect(Collectors.toList());
    }

    public void updateMeal(MealRequest mealRequest) {
    }
}
