package org.pragma.restaurantplaza.application.dto;

import lombok.Getter;
import lombok.Setter;
import org.pragma.restaurantplaza.domain.model.Owner;
@Getter
@Setter
public class RestaurantResponse {

    private String name;
    private String nit;
    private String address;
    private String phone;
    private String urlLogo;
    private Owner ownerId;
}