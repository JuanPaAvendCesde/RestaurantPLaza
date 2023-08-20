package org.pragma.restaurantplaza.infrastructure.output.jpa.mapper;

import org.mapstruct.Mapper;
import org.pragma.restaurantplaza.domain.model.Owner;
import org.pragma.restaurantplaza.domain.model.Restaurant;
import org.pragma.restaurantplaza.infrastructure.output.jpa.entity.RestaurantEntity;

import java.util.List;

@Mapper(componentModel = "spring",
unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE,
        unmappedSourcePolicy = org.mapstruct.ReportingPolicy.IGNORE )
public interface RestaurantEntityMapper {

    RestaurantEntity toRestaurantEntity(Restaurant restaurant);
    Owner toOwner(RestaurantEntity restaurantEntity);

    List<Restaurant> toRestaurantList(List<RestaurantEntity> restaurantEntityList);
}
