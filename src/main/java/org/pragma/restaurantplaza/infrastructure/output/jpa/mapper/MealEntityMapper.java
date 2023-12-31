package org.pragma.restaurantplaza.infrastructure.output.jpa.mapper;

import org.mapstruct.Mapper;
import org.pragma.restaurantplaza.application.dto.MealRequest;
import org.pragma.restaurantplaza.application.dto.MealResponse;
import org.pragma.restaurantplaza.domain.model.Meal;
import org.pragma.restaurantplaza.infrastructure.output.jpa.entity.MealEntity;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE,
        unmappedSourcePolicy = org.mapstruct.ReportingPolicy.IGNORE)
public interface MealEntityMapper {

    MealEntity toMealEntity(MealRequest mealRequest);

    MealResponse toMealResponse(MealEntity mealEntity);
}
