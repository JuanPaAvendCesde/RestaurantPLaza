package org.pragma.restaurantplaza.application.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class MealRequest {

    private String name;
    private int price;
    private String description;
    private String urlImage;
    private String category;
}
