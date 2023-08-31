package org.pragma.restaurantplaza.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class MealResponse {

    private String name;
    private int price;
    private String description;
    private String urlImage;
    private String category;
}
