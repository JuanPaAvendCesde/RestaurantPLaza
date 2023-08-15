package org.pragma.restaurantplaza.domain.spi;

import org.pragma.restaurantplaza.domain.model.Meal;


public interface IMealPersistencePort {


    void saveMeal(Meal meal);

    void updateMeal(Long mealId, int newPrice, String newDescription);
}