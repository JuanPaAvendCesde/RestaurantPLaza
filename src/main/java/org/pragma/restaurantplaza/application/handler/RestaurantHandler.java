package org.pragma.restaurantplaza.application.handler;

import lombok.RequiredArgsConstructor;
import org.pragma.restaurantplaza.application.dto.RestaurantRequest;
import org.pragma.restaurantplaza.application.dto.UserRequest;
import org.pragma.restaurantplaza.application.mapper.OwnerRequestMapper;
import org.pragma.restaurantplaza.application.mapper.RestaurantRequestMapper;
import org.pragma.restaurantplaza.domain.api.IRestaurantServicePort;
import org.pragma.restaurantplaza.domain.model.Restaurant;
import org.pragma.restaurantplaza.domain.model.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class RestaurantHandler implements IRestaurantHandler {

    private final IRestaurantServicePort restaurantServicePort;
    private final RestaurantRequestMapper restaurantRequestMapper;

    private final OwnerRequestMapper ownerRequestMapper;

    @Override
    public void saveRestaurant(RestaurantRequest restaurantRequest, UserRequest userRequest) {
        Restaurant restaurant = restaurantRequestMapper.toRestaurant(restaurantRequest);
        User user = ownerRequestMapper.toOwner(userRequest);
        restaurantServicePort.saveRestaurant(restaurant, user);
    }
}
