package org.pragma.restaurantplaza.application.dto;

import lombok.Getter;
import lombok.Setter;
import org.pragma.restaurantplaza.domain.model.Restaurant;

@Setter
@Getter

public class MealResponse {

    private String name;
    private int price;
    private String description;
    private String urlImage;

}
