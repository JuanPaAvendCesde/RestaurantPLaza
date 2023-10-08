package org.pragma.restaurantplaza.application.dto;

import lombok.Getter;
import lombok.Setter;
import org.pragma.restaurantplaza.infrastructure.output.jpa.entity.UserEntity;

@Getter
@Setter

public class RestaurantResponse {

    private Long id;
    private String name;
    private String urlLogo;


    public static RestaurantResponse fromRestaurant(RestaurantResponse restaurant) {
        RestaurantResponse response = new RestaurantResponse();
        response.setId(restaurant.getId());
        response.setName(restaurant.getName());
        return response;
    }
}
