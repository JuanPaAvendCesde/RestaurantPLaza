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
    public void saveMeal(MealRequest mealRequest, UserRequest userRequest) {
        Meal meal = mealRequestMapper.toMeal(mealRequest);
        User user = userRequestMapper.toOwner(userRequest);
        mealServicePort.saveMeal(meal, user);
    }

    public void updateMeal(Long mealId, int newPrice, String newDescription) {
        mealAdapter.updateMeal(mealId, newPrice, newDescription);
    }


    public boolean changeMealStatus(boolean active) {
        return active;
    }
}
