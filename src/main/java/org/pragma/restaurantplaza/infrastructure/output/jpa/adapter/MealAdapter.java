package org.pragma.restaurantplaza.infrastructure.output.jpa.adapter;

import lombok.RequiredArgsConstructor;
import org.pragma.restaurantplaza.application.dto.MealRequest;
import org.pragma.restaurantplaza.application.dto.MealResponse;
import org.pragma.restaurantplaza.application.dto.RestaurantResponse;
import org.pragma.restaurantplaza.domain.spi.IMealPersistencePort;
import org.pragma.restaurantplaza.infrastructure.exception.*;
import org.pragma.restaurantplaza.infrastructure.output.jpa.entity.MealEntity;
import org.pragma.restaurantplaza.infrastructure.output.jpa.entity.RestaurantEntity;
import org.pragma.restaurantplaza.infrastructure.output.jpa.mapper.MealEntityMapper;
import org.pragma.restaurantplaza.infrastructure.output.jpa.repository.IMealRepository;
import org.pragma.restaurantplaza.infrastructure.output.jpa.repository.IRestaurantRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@RequiredArgsConstructor
public class MealAdapter implements IMealPersistencePort {

    private static final String ROLE = "OWNER";

    private final IMealRepository mealRepository;

    private final MealEntityMapper mealEntityMapper;

    private final IRestaurantRepository restaurantRepository;

    @Override
    public void saveMeal(MealRequest meal) {
        if (mealRepository.findById(meal.getId()).isPresent()) {
            throw new UserAlreadyExistException("meal already exists");
        }

        if (meal.getPrice() <= 0) {
            throw new InvalidMealPriceException("Meal price must be a positive number greater than 0");
        }

        RestaurantEntity restaurant = restaurantRepository.findById(meal.getRestaurantId().getId())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        mealRepository.save(mealEntityMapper.toMealEntity(meal));
    }

    @Override
    public void updateMeal(MealRequest meal) {

        MealEntity existingMeal = mealRepository.findById(meal.getId())
                .orElseThrow(() -> new EntityNotFoundException("Meal not found"));


        if (meal.getPrice() > 0) {
            existingMeal.setPrice(meal.getPrice());
        }
        if (meal.getDescription() != null) {
            existingMeal.setDescription(meal.getDescription());
        }


        mealRepository.save(existingMeal);
    }


    public void changeMealStatusById(Long mealId, boolean active) {
        MealEntity mealEntity = mealRepository.findById(mealId)
                .orElseThrow(() -> new MealNotFoundException("Meal not found"));

        mealEntity.setActive(active);
        mealRepository.save(mealEntity);
    }

    @Override
    public MealResponse mapToMealResponse(MealEntity mealEntity) {
        MealResponse mealResponse = new MealResponse();
        mealResponse.setName(mealEntity.getName());
        mealResponse.setPrice(mealEntity.getPrice());
        mealResponse.setDescription(mealEntity.getDescription());
        mealResponse.setUrlImage(mealEntity.getUrlImage());

        return mealResponse;
    }

    public Page<MealResponse> getMenuByRestaurant(RestaurantEntity restaurantEntity, String category, Pageable pageable) {
        return mealRepository.findByRestaurantIdAndCategoryAndActiveTrue(restaurantEntity, category, pageable)
                .map(this::mapToMealResponse);
    }
}
