package org.pragma.restaurantplaza.application.handler;

import org.pragma.restaurantplaza.application.dto.MealRequest;
import org.pragma.restaurantplaza.application.dto.UserRequest;

public interface IMealHandler {
    void saveMeal(MealRequest mealRequest);
}
