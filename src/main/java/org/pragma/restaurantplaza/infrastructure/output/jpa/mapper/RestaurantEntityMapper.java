package org.pragma.restaurantplaza.infrastructure.output.jpa.mapper;

import org.mapstruct.Mapper;
import org.pragma.restaurantplaza.domain.model.Restaurant;
import org.pragma.restaurantplaza.infrastructure.output.jpa.entity.RestaurantEntity;


@Mapper(componentModel = "spring",
        unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE,
        unmappedSourcePolicy = org.mapstruct.ReportingPolicy.IGNORE)
public interface RestaurantEntityMapper {

    RestaurantEntity toRestaurantEntity(Restaurant restaurant);

    Restaurant toRestaurant(RestaurantEntity restaurantEntity);

}
