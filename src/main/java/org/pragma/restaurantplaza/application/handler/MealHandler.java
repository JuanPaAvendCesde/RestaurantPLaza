package org.pragma.restaurantplaza.application.handler;

import lombok.RequiredArgsConstructor;
import org.pragma.restaurantplaza.application.dto.MealRequest;
import org.pragma.restaurantplaza.application.mapper.MealResquestMapper;
import org.pragma.restaurantplaza.domain.api.IMealServicePort;
import org.pragma.restaurantplaza.domain.model.Meal;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class MealHandler implements IMealHandler{


    private final IMealServicePort mealServicePort;

    private final MealResquestMapper mealRequestMapper;

    private final MealResquestMapper mealResquestMapper;




    @Override
    public void saveMeal(MealRequest mealRequest) {
        Meal meal = mealResquestMapper.toMeal(mealRequest);
        mealServicePort.saveMeal(meal);
    }
}
