package org.pragma.restaurantplaza.domain.api;

import org.pragma.restaurantplaza.domain.model.Meal;


public interface IMealServicePort {

    void saveMeal(Meal meal);
}
