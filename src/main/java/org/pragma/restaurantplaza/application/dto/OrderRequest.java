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

    private int quantity;
}
