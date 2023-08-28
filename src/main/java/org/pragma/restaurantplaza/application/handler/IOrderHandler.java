package org.pragma.restaurantplaza.application.handler;

import org.pragma.restaurantplaza.application.dto.OrderResponse;
import org.pragma.restaurantplaza.domain.model.OrderStatus;
import org.pragma.restaurantplaza.domain.model.Restaurant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IOrderHandler {
    Page<OrderResponse> getOrdersByStateAndRestaurant(OrderStatus state, Restaurant restaurant, Pageable pageable);
}
