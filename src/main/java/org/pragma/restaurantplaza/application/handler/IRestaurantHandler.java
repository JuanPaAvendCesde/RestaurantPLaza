package org.pragma.restaurantplaza.application.handler;



import org.pragma.restaurantplaza.application.dto.OwnerRequest;
import org.pragma.restaurantplaza.application.dto.RestaurantRequest;



public interface IRestaurantHandler  {
    void saveRestaurant(RestaurantRequest restaurantRequest, OwnerRequest ownerRequest);

}
