package org.pragma.restaurantplaza.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.pragma.restaurantplaza.domain.model.OrderStatus;
import org.pragma.restaurantplaza.domain.model.Restaurant;
import org.pragma.restaurantplaza.domain.model.User;
import org.pragma.restaurantplaza.infrastructure.output.jpa.entity.MealEntity;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class OrderResponse {
    private Long id;

    private User user;

    private Restaurant restaurant;

    private List<MealEntity> meals;

    private OrderStatus orderStatus;
    private Long assignedEmployeeId;
    private int quantity;
    private String securityPin;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;


}
