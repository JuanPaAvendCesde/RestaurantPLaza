package org.pragma.restaurantplaza.application.dto;

import lombok.Getter;
import lombok.Setter;
import org.pragma.restaurantplaza.domain.model.OrderStatus;
import org.pragma.restaurantplaza.domain.model.Restaurant;
import org.pragma.restaurantplaza.domain.model.User;
import org.pragma.restaurantplaza.infrastructure.output.jpa.entity.MealEntity;

import java.util.List;

@Getter
@Setter
public class OrderRequest {
    private Long id;

    private User user;

    private Restaurant restaurant;

    private List<MealEntity> meals;

    private OrderStatus orderStatus;
    private Long assignedEmployeeId;
    private int quantity;

    public OrderRequest(Long id, User user, Restaurant restaurant, List<MealEntity> meals, OrderStatus orderStatus, Long assignedEmployeeId, int quantity) {
        this.id = id;
        this.user = user;
        this.restaurant = restaurant;
        this.meals = meals;
        this.orderStatus = orderStatus;
        this.assignedEmployeeId = assignedEmployeeId;
        this.quantity = quantity;
    }
}
