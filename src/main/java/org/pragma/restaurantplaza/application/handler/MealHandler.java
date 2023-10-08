package org.pragma.restaurantplaza.application.handler;

import lombok.RequiredArgsConstructor;
import org.pragma.restaurantplaza.application.dto.MealRequest;
import org.pragma.restaurantplaza.application.dto.UserRequest;
import org.pragma.restaurantplaza.application.mapper.MealRequestMapper;
import org.pragma.restaurantplaza.application.mapper.UserRequestMapper;
import org.pragma.restaurantplaza.domain.api.IMealServicePort;
import org.pragma.restaurantplaza.domain.model.Meal;
import org.pragma.restaurantplaza.domain.model.User;
import org.pragma.restaurantplaza.infrastructure.output.jpa.adapter.MealAdapter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class MealHandler implements IMealHandler {

    private final IMealServicePort mealServicePort;

    private final MealRequestMapper mealRequestMapper;

    private final MealAdapter mealAdapter;

    private final UserRequestMapper userRequestMapper;


    @Override
    public void saveMeal(MealRequest mealRequest) {
        Meal meal = mealRequestMapper.toMeal(mealRequest);

        mealServicePort.saveMeal(mealRequest);
    }

    public void updateMeal(MealRequest mealRequest) {
        mealAdapter.updateMeal(mealRequest);
    }




    public void changeMealStatusById(Long mealId, boolean active) {
        mealAdapter.changeMealStatusById(mealId, active);
    }
}
