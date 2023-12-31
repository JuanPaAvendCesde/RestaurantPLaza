package org.pragma.restaurantplaza.domain.usecase;

import org.pragma.restaurantplaza.application.dto.MealRequest;
import org.pragma.restaurantplaza.domain.api.IMealServicePort;
import org.pragma.restaurantplaza.domain.model.Meal;
import org.pragma.restaurantplaza.domain.model.User;
import org.pragma.restaurantplaza.domain.spi.IMealPersistencePort;

public class MealUseCase implements IMealServicePort {
    private final IMealPersistencePort mealPersistencePort;

    public MealUseCase(IMealPersistencePort mealPersistencePor) {
        this.mealPersistencePort = mealPersistencePor;
    }
    @Override
    public void saveMeal(MealRequest meal) {
        mealPersistencePort.saveMeal(meal);
    }
}
