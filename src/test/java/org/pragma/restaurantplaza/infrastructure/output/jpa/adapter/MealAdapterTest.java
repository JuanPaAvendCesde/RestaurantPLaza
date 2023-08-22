package org.pragma.restaurantplaza.infrastructure.output.jpa.adapter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.pragma.restaurantplaza.domain.model.Meal;
import org.pragma.restaurantplaza.domain.model.Restaurant;
import org.pragma.restaurantplaza.domain.model.User;
import org.pragma.restaurantplaza.infrastructure.exception.InvalidMealPriceException;
import org.pragma.restaurantplaza.infrastructure.exception.InvalidUserRoleException;
import org.pragma.restaurantplaza.infrastructure.exception.MealNotFoundException;
import org.pragma.restaurantplaza.infrastructure.exception.UserAlreadyExistException;
import org.pragma.restaurantplaza.infrastructure.output.jpa.entity.MealEntity;
import org.pragma.restaurantplaza.infrastructure.output.jpa.entity.RestaurantEntity;
import org.pragma.restaurantplaza.infrastructure.output.jpa.entity.UserEntity;
import org.pragma.restaurantplaza.infrastructure.output.jpa.mapper.MealEntityMapper;
import org.pragma.restaurantplaza.infrastructure.output.jpa.repository.IMealRepository;

import java.time.LocalDate;
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
        User user = new User(1L, "user", 3265326, "+573226094632", LocalDate.of(1995, 8, 20), "aa@aa.com", "12334", "OWNER");
        Restaurant restaurant = new Restaurant(1L, "name", 213123, "rewrite", "+573005698325", "", user, null);
        Meal meal = new Meal(1L, "Sombre de la comical", 123, "Description", "Ingredients", "URL de la image", restaurant);
        MealEntity mealEntity = new MealEntity();

        when(mealRepository.findById(1L)).thenReturn(Optional.empty());
        when(mealEntityMapper.toMealEntity(meal)).thenReturn(mealEntity);

        assertDoesNotThrow(() -> mealAdapter.saveMeal(meal, user));
        verify(mealRepository, times(1)).findById(1L);
        verify(mealEntityMapper, times(1)).toMealEntity(meal);
        verify(mealRepository, times(1)).save(mealEntity);
    }

    @Test
    void saveMeal_UnsuccessfulCreation() {
        User user = new User(1L, "user", 3265326, "+573226094632", LocalDate.of(1995, 8, 20), "aa@aa.com", "12334", "CLIENT");
        Restaurant restaurant = new Restaurant(1L, "name", 213123, "rewrite", "+573005698325", "", user, null);
        Meal meal = new Meal(1L, "Sombre de la comical", 123, "Description", "Ingredients", "URL de la image", restaurant);
        MealEntity mealEntity = new MealEntity();

        when(mealRepository.findById(1L)).thenReturn(Optional.empty());
        when(mealEntityMapper.toMealEntity(meal)).thenReturn(mealEntity);

        InvalidUserRoleException exception = assertThrows(
                InvalidUserRoleException.class,
                () -> mealAdapter.saveMeal(meal, user)
        );
        assertEquals("User must have the 'Owner' role to create a meal", exception.getMessage());
        verify(mealRepository, times(1)).findById(1L);
    }

    @Test
    void saveMeal_AlreadyExists() {
        User user = new User(1L, "user", 3265326, "+573226094632", LocalDate.of(1995, 8, 20), "aa@aa.com", "12334", "OWNER");
        Restaurant restaurant = new Restaurant(1L, "name", 213123, "rewrite", "+573005698325", "", user, null);
        Meal meal = new Meal(1L, "Sombre de la comical", 123, "Description", "Ingredients", "URL de la image", restaurant);

        when(mealRepository.findById(1L)).thenReturn(Optional.of(new MealEntity()));

        assertThrows(UserAlreadyExistException.class, () -> mealAdapter.saveMeal(meal, user));
        verify(mealRepository, times(1)).findById(1L);
        verifyNoMoreInteractions(mealEntityMapper);
        verifyNoMoreInteractions(mealRepository);
    }

    @Test
    void updateMeal_SuccessfulUpdate() {
        long mealId = 1L;
        int newPrice = 12345;
        String newDescription = "New description";
        MealEntity existingMealEntity = new MealEntity();
        existingMealEntity.setPrice(100);
        existingMealEntity.setDescription("Old description");
        existingMealEntity.setRestaurantId(new RestaurantEntity());
        existingMealEntity.getRestaurantId().setUserId(new UserEntity());
        existingMealEntity.getRestaurantId().getUserId().setRole("OWNER");

        when(mealRepository.findById(mealId)).thenReturn(Optional.of(existingMealEntity));

        mealAdapter.updateMeal(mealId, newPrice, newDescription);

        verify(mealRepository, times(1)).findById(mealId);
        verify(mealRepository, times(1)).save(existingMealEntity);
        assertEquals(newPrice, existingMealEntity.getPrice());
        assertEquals(newDescription, existingMealEntity.getDescription());
    }

    @Test
    void updateMeal_UnsuccessfulUpdate() {

        long mealId = 1L;
        int newPrice = 12345;
        String newDescription = "New description";
        MealEntity existingMealEntity = new MealEntity();
        existingMealEntity.setPrice(100);
        existingMealEntity.setDescription("Old description");
        existingMealEntity.setRestaurantId(new RestaurantEntity());
        existingMealEntity.getRestaurantId().setUserId(new UserEntity());
        existingMealEntity.getRestaurantId().getUserId().setRole("USER");

        when(mealRepository.findById(mealId)).thenReturn(Optional.of(existingMealEntity));

        InvalidUserRoleException exception = assertThrows(
                InvalidUserRoleException.class,
                () -> mealAdapter.updateMeal(mealId, newPrice, newDescription)
        );

        assertEquals("User must have the 'Owner' role to update a meal", exception.getMessage());
        verify(mealRepository, times(1)).findById(mealId);
        verify(mealRepository, times(0)).save(existingMealEntity);
    }

    @Test
    void saveMeal_InvalidMealPrice() {

        User user = new User(1L, "user", 3265326, "+573226094632", LocalDate.of(1995, 8, 20), "aa@aa.com", "12334", "OWNER");
        Restaurant restaurant = new Restaurant(1L, "name", 213123, "rewrite", "+573005698325", "", user, null);
        Meal meal = new Meal(1L, "Sombre de la comical", -65123, "Description", "Ingredients", "URL de la image", restaurant);

        when(mealRepository.findById(user.getId())).thenReturn(Optional.empty());
        InvalidMealPriceException exception = assertThrows(
                InvalidMealPriceException.class,
                () -> mealAdapter.saveMeal(meal, user)
        );

        assertEquals("Meal price must be a positive number greater than 0", exception.getMessage());
        verify(mealRepository, times(1)).findById(user.getId());
    }

    @Test
    void changeMealStatus_ValidMealIdAndRole_ActiveStatusChanged() {

        Long mealId = 1L;
        boolean active = true;
        User owner = new User(1L, "user", 3265326, "+573226094632", LocalDate.of(1995, 8, 20), "aa@aa.com", "12334", "OWNER");
        owner.setRole("OWNER");
        MealEntity existingMealEntity = new MealEntity();
        existingMealEntity.setId(mealId);
        existingMealEntity.setActive(!active);
        RestaurantEntity restaurantEntity = new RestaurantEntity();
        restaurantEntity.setUserId(new UserEntity());
        restaurantEntity.getUserId().setRole("OWNER");
        existingMealEntity.setRestaurantId(restaurantEntity);

        when(mealRepository.findById(mealId)).thenReturn(Optional.of(existingMealEntity));
        MealAdapter mealAdapter = new MealAdapter(mealRepository, mealEntityMapper);
        assertDoesNotThrow(() -> mealAdapter.changeMealStatus(mealId, active));

        assertTrue(existingMealEntity.isActive());
        verify(mealRepository, times(1)).findById(mealId);
        verify(mealRepository, times(1)).save(existingMealEntity);
    }

    @Test
    void changeMealStatus_InvalidRole_ThrowsInvalidUserRoleException() {

        Long mealId = 1L;
        boolean active = true;
        MealEntity existingMealEntity = new MealEntity();
        existingMealEntity.setId(mealId);
        RestaurantEntity restaurantEntity = new RestaurantEntity();
        UserEntity userEntity = new UserEntity();
        userEntity.setRole("Client");
        restaurantEntity.setUserId(userEntity);
        existingMealEntity.setRestaurantId(restaurantEntity);

        when(mealRepository.findById(mealId)).thenReturn(Optional.of(existingMealEntity));
        MealAdapter mealAdapter = new MealAdapter(mealRepository, mealEntityMapper);
        InvalidUserRoleException exception = assertThrows(
                InvalidUserRoleException.class,
                () -> mealAdapter.changeMealStatus(mealId, active)
        );

        assertEquals("Invalid user role", exception.getMessage());
    }

    @Test
    void changeMealStatus_MealNotFound_ThrowsMealNotFoundException() {

        Long mealId = 1L;
        boolean active = true;

        when(mealRepository.findById(mealId)).thenReturn(Optional.empty());
        MealAdapter mealAdapter = new MealAdapter(mealRepository, mealEntityMapper);

        assertThrows(MealNotFoundException.class, () -> mealAdapter.changeMealStatus(mealId, active));
        verify(mealRepository, times(1)).findById(mealId);
        verify(mealRepository, times(0)).save(any(MealEntity.class));
    }
}
