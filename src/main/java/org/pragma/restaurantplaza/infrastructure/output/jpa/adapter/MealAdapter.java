package org.pragma.restaurantplaza.infrastructure.output.jpa.adapter;

import lombok.RequiredArgsConstructor;
import org.pragma.restaurantplaza.domain.model.Meal;
import org.pragma.restaurantplaza.domain.spi.IMealPersistencePort;
import org.pragma.restaurantplaza.infrastructure.exception.OwnerAlreadyExistException;
import org.pragma.restaurantplaza.infrastructure.output.jpa.mapper.MealEntityMapper;
import org.pragma.restaurantplaza.infrastructure.output.jpa.repository.IMealRepository;
@RequiredArgsConstructor
public class MealAdapter implements IMealPersistencePort {

    private final IMealRepository mealRepository;

    private final MealEntityMapper mealEntityMapper;
    @Override
    public void saveMeal(Meal meal) {

        if(mealRepository.findById(meal.getId()).isPresent() ) {
            throw new OwnerAlreadyExistException("meal already exists");
        }

        mealRepository.save(mealEntityMapper.toMealEntity(meal));


    }
}
