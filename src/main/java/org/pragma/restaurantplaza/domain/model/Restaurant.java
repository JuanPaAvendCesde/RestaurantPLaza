package org.pragma.restaurantplaza.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.pragma.restaurantplaza.infrastructure.output.jpa.entity.MealEntity;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class Restaurant {

    private Long id;
    private String name;
    private Integer nit;
    private String address;
    private String phone;
    private String urlLogo;
    private User userId;
    private List<MealEntity> meals;



}
