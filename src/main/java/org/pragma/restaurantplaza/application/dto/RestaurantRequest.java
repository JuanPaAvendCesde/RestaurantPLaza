package org.pragma.restaurantplaza.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.pragma.restaurantplaza.infrastructure.output.jpa.entity.MealEntity;
import org.pragma.restaurantplaza.infrastructure.output.jpa.entity.UserEntity;

import java.util.List;

@Getter
@Setter

public class RestaurantRequest {

    private Long id;

    private String name;

    private Integer nit;

    private String address;

    private String phone;

    private String urlLogo;

    private UserEntity ownerId;

}
