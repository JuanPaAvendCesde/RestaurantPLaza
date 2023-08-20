package org.pragma.restaurantplaza.infrastructure.output.jpa.adapter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.pragma.restaurantplaza.domain.model.Meal;
import org.pragma.restaurantplaza.infrastructure.exception.OwnerAlreadyExistException;
import org.pragma.restaurantplaza.infrastructure.output.jpa.entity.MealEntity;
import org.pragma.restaurantplaza.infrastructure.output.jpa.mapper.MealEntityMapper;
import org.pragma.restaurantplaza.infrastructure.output.jpa.repository.IMealRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MealAdapterTest {

    @Mock
    private IMealRepository mealRepository;

    @Mock
    private MealEntityMapper mealEntityMapper;

    private MealAdapter mealAdapter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mealAdapter = new MealAdapter(mealRepository, mealEntityMapper);
    }


    @Test
    void saveMeal_SuccessfulCreation() {

        Meal meal = new Meal(1L, "Nombre de la comida", 123, "Descripción", "Ingredientes", "URL de la imagen");
        MealEntity mealEntity = new MealEntity();
        when(mealRepository.findById(1L)).thenReturn(Optional.empty());
        when(mealEntityMapper.toMealEntity(meal)).thenReturn(mealEntity);

        assertDoesNotThrow(() -> mealAdapter.saveMeal(meal));
        verify(mealRepository, times(1)).findById(1L);
        verify(mealEntityMapper, times(1)).toMealEntity(meal);
        verify(mealRepository, times(1)).save(mealEntity);
    }

    @Test
    void saveMeal_AlreadyExists() {

        Meal meal = new Meal(1L, "Nombre de la comida", 123, "Descripción", "Ingredientes", "URL de la imagen");

        when(mealRepository.findById(1L)).thenReturn(Optional.of(new MealEntity()));

        assertThrows(OwnerAlreadyExistException.class, () -> mealAdapter.saveMeal(meal));
        verify(mealRepository, times(1)).findById(1L);
        verifyNoMoreInteractions(mealEntityMapper);
        verifyNoMoreInteractions(mealRepository);
    }

    @Test
    void updateMeal_SuccessfulUpdate() {
        // Arrange
        long mealId = 1L;
        int newPrice = 12345;
        String newDescription = "New description";

        MealEntity existingMeal = new MealEntity();
        existingMeal.setId(mealId);

        when(mealRepository.findById(mealId)).thenReturn(Optional.of(existingMeal));

        // Act
        mealAdapter.updateMeal(mealId, newPrice, newDescription);

        // Assert
        verify(mealRepository, times(1)).findById(mealId);
        verify(mealRepository, times(1)).save(existingMeal);
        assertEquals(newPrice, existingMeal.getPrice());
        assertEquals(newDescription, existingMeal.getDescription());
    }
}