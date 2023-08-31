package org.pragma.restaurantplaza.infrastructure.output.jpa.adapter;

import lombok.RequiredArgsConstructor;
import org.pragma.restaurantplaza.application.dto.MealResponse;
import org.pragma.restaurantplaza.domain.model.Meal;
import org.pragma.restaurantplaza.domain.model.User;
import org.pragma.restaurantplaza.domain.spi.IMealPersistencePort;
import org.pragma.restaurantplaza.infrastructure.exception.InvalidMealPriceException;
import org.pragma.restaurantplaza.infrastructure.exception.InvalidUserRoleException;
import org.pragma.restaurantplaza.infrastructure.exception.MealNotFoundException;
import org.pragma.restaurantplaza.infrastructure.exception.UserAlreadyExistException;
import org.pragma.restaurantplaza.infrastructure.output.jpa.entity.MealEntity;
import org.pragma.restaurantplaza.infrastructure.output.jpa.entity.RestaurantEntity;
import org.pragma.restaurantplaza.infrastructure.output.jpa.mapper.MealEntityMapper;
import org.pragma.restaurantplaza.infrastructure.output.jpa.repository.IMealRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.Optional;

@RequiredArgsConstructor
public class MealAdapter implements IMealPersistencePort {

    private static final String ROLE = "OWNER";

    private final IMealRepository mealRepository;

    private final MealEntityMapper mealEntityMapper;

    @Override
    public void saveMeal(Meal meal, User user) {
        if (mealRepository.findById(meal.getId()).isPresent()) {
            throw new UserAlreadyExistException("meal already exists");
        }
        if (!user.getRole().equals(ROLE)) {
            throw new InvalidUserRoleException("User must have the 'Owner' role to create a meal");
        }
        if (meal.getPrice() <= 0) {
            throw new InvalidMealPriceException("Meal price must be a positive number greater than 0");
        }
        mealRepository.save(mealEntityMapper.toMealEntity(meal));
    }

    @Override
    public void updateMeal(Long mealId, int newPrice, String newDescription) {
        Optional<MealEntity> existingMeal = mealRepository.findById(mealId);
        if (existingMeal.isEmpty()) {
            throw new MealNotFoundException("Meal not found");
        }

        if (!existingMeal.get().getRestaurantId().getUserId().getRole().equals(ROLE)) {
            throw new InvalidUserRoleException("User must have the 'Owner' role to update a meal");
        }

        MealEntity mealEntity = existingMeal.get();
        mealEntity.setPrice(newPrice);
        mealEntity.setDescription(newDescription);
        mealRepository.save(mealEntity);
    }
    @Override
    public void changeMealStatus(Long mealId, boolean active) {
        Optional<MealEntity> existingMeal = mealRepository.findById(mealId);
        if (existingMeal.isEmpty()) {
            throw new MealNotFoundException("Meal not found");
        }
        if (!existingMeal.get().getRestaurantId().getUserId().getRole().equals(ROLE)) {
            throw new InvalidUserRoleException("Invalid user role");
        }
        MealEntity mealEntity = existingMeal.get();
        mealEntity.setActive(active);
        mealRepository.save(mealEntity);
    }

    public Page<MealResponse> getRestaurantMenuByCategory(RestaurantEntity restaurant, String category, int page, int size) {
        Page<MealEntity> mealEntityPage = mealRepository.findByRestaurantIdAndCategory( restaurant,category, PageRequest.of(page, size));
        return mealEntityPage.map(this::mapToMealResponse);
    }

    private MealResponse mapToMealResponse(MealEntity mealEntity) {
        return new MealResponse(  mealEntity.getName(), mealEntity.getPrice(),mealEntity.getDescription(), mealEntity.getUrlImage(), mealEntity.getCategory());
    }
}
