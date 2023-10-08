package org.pragma.restaurantplaza.domain.spi;

import org.pragma.restaurantplaza.application.dto.MealRequest;
import org.pragma.restaurantplaza.application.dto.MealResponse;
import org.pragma.restaurantplaza.domain.model.Meal;
import org.pragma.restaurantplaza.domain.model.User;
import org.pragma.restaurantplaza.infrastructure.output.jpa.entity.MealEntity;


public interface IMealPersistencePort {


    void saveMeal(MealRequest meal);

    void updateMeal(MealRequest meal);


    void changeMealStatusById(Long mealId, boolean active);

    MealResponse mapToMealResponse(MealEntity mealEntity);
}