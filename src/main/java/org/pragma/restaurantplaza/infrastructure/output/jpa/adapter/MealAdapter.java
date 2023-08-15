package org.pragma.restaurantplaza.infrastructure.output.jpa.adapter;

import lombok.RequiredArgsConstructor;
import org.pragma.restaurantplaza.domain.model.Meal;
import org.pragma.restaurantplaza.domain.spi.IMealPersistencePort;
import org.pragma.restaurantplaza.infrastructure.exception.OwnerAlreadyExistException;
import org.pragma.restaurantplaza.infrastructure.output.jpa.entity.MealEntity;
import org.pragma.restaurantplaza.infrastructure.output.jpa.mapper.MealEntityMapper;
import org.pragma.restaurantplaza.infrastructure.output.jpa.repository.IMealRepository;

import java.util.Optional;

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

    @Override
    public void updateMeal(Long mealId, int newPrice, String newDescription) {
        Optional<MealEntity> existingMeal = mealRepository.findById(mealId);
        if (existingMeal.isPresent()) {
            MealEntity mealEntity = existingMeal.get();
            mealEntity.setPrice(newPrice);
            mealEntity.setDescription(newDescription);
            mealRepository.save(mealEntity);
        }
    }

}
