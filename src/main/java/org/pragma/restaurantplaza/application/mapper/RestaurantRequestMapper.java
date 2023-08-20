package org.pragma.restaurantplaza.application.mapper;

import org.mapstruct.Mapper;
import org.pragma.restaurantplaza.application.dto.RestaurantRequest;
import org.pragma.restaurantplaza.domain.model.Restaurant;

@Mapper(componentModel = "spring",
unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE,
        unmappedSourcePolicy = org.mapstruct.ReportingPolicy.IGNORE )
public interface RestaurantRequestMapper {

    Restaurant toRestaurant(RestaurantRequest restaurantRequest);
}
