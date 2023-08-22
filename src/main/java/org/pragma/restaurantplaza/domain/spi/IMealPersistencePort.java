package org.pragma.restaurantplaza.domain.spi;

import org.pragma.restaurantplaza.domain.model.Meal;
import org.pragma.restaurantplaza.domain.model.User;


public interface IMealPersistencePort {


    void saveMeal(Meal meal, User user);

    void updateMeal(Long mealId, int newPrice, String newDescription);


}
