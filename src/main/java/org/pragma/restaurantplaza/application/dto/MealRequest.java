package org.pragma.restaurantplaza.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.pragma.restaurantplaza.domain.model.Order;
import org.pragma.restaurantplaza.domain.model.Restaurant;

@Setter
@Getter
@AllArgsConstructor
public class MealRequest {
    private Long id;
    private String name;
    private int price;
    private String description;
    private String urlImage;
    private String category;
    private Restaurant restaurantId;



}
