package org.pragma.restaurantplaza.application.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.pragma.restaurantplaza.application.dto.MealRequest;
import org.pragma.restaurantplaza.domain.model.Meal;


@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface MealResquestMapper {

   Meal toMeal(MealRequest mealRequest);
}
