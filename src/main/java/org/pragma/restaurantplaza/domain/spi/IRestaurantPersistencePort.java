package org.pragma.restaurantplaza.domain.spi;

import org.pragma.restaurantplaza.application.dto.MealResponse;
import org.pragma.restaurantplaza.domain.model.Order;
import org.pragma.restaurantplaza.domain.model.OrderStatus;
import org.pragma.restaurantplaza.domain.model.Restaurant;
import org.pragma.restaurantplaza.domain.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;


public interface IRestaurantPersistencePort {
    void saveRestaurant(Restaurant restaurant, User user);

    //crear controla
    List<MealResponse> getRestaurantMenu(Long restaurantId);

    Page<MealResponse> getRestaurantMenuByCategory(Long restaurantId, String name, String category, int page, int size);


    Page<Restaurant> findAll(Pageable pageable);

    Page<Order> getOrdersByStateAndRestaurant(OrderStatus state, Restaurant restaurant, Pageable pageable);

    Page<Order> getAssignedOrdersByStateAndRestaurant(OrderStatus state, Long employeeId, Long restaurantId, Pageable pageable);
}
