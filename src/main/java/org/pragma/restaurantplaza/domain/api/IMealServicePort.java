package org.pragma.restaurantplaza.domain.api;

import org.pragma.restaurantplaza.domain.model.Meal;
import org.pragma.restaurantplaza.domain.model.User;

public interface IMealServicePort {
    void saveMeal(Meal meal, User user);
}
