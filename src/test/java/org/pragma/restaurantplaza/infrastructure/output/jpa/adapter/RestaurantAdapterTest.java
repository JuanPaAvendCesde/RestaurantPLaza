package org.pragma.restaurantplaza.infrastructure.output.jpa.adapter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.pragma.restaurantplaza.domain.model.Restaurant;
import org.pragma.restaurantplaza.domain.model.User;
import org.pragma.restaurantplaza.infrastructure.exception.*;
import org.pragma.restaurantplaza.infrastructure.output.jpa.entity.RestaurantEntity;
import org.pragma.restaurantplaza.infrastructure.output.jpa.mapper.RestaurantEntityMapper;
import org.pragma.restaurantplaza.infrastructure.output.jpa.repository.IMealRepository;
import org.pragma.restaurantplaza.infrastructure.output.jpa.repository.IRestaurantRepository;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RestaurantAdapterTest {

    @Mock
    private IRestaurantRepository restaurantRepository;

    @Mock
    private RestaurantEntityMapper restaurantEntityMapper;

    @Mock
    private IMealRepository mealRepository;

    private RestaurantAdapter restaurantAdapter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        restaurantAdapter = new RestaurantAdapter(restaurantRepository, restaurantEntityMapper, mealRepository);
    }

    @Test
    void saveRestaurant_SuccessfulCreation() {

        LocalDate birthdate = LocalDate.of(1995, 8, 20);
        User user = new User(1L, "user", 3265326, "+573226094632", birthdate, "aa@aa.com", "12334", "OWNER");
        Restaurant restaurant = new Restaurant(1L, "name", 213123, "newsdealer", "+573005698325", "", user, null);

        when(restaurantRepository.findById(1L)).thenReturn(Optional.empty());
        when(restaurantEntityMapper.toRestaurantEntity(restaurant)).thenReturn(new RestaurantEntity());
        when(restaurantRepository.findById(1L)).thenReturn(Optional.empty());
        when(restaurantEntityMapper.toRestaurantEntity(restaurant)).thenReturn(new RestaurantEntity());

        assertDoesNotThrow(() -> restaurantAdapter.saveRestaurant(restaurant, user));
        verify(restaurantRepository, times(1)).findById(1L);
        verify(restaurantEntityMapper, times(1)).toRestaurantEntity(restaurant);
        verify(restaurantRepository, times(1)).save(any(RestaurantEntity.class));
    }

    @Test
    void saveRestaurant_RestaurantAlreadyExists() {

        User user = new User(1L, "user", 3265326, "2132152", LocalDate.of(1995, 8, 20), "aa@aa.com", "12334", "OWNER");
        Restaurant restaurant = new Restaurant(1L, "name", 213123, "rewritable", "+573005698325", "", user, null);

        when(restaurantRepository.findById(1L)).thenReturn(Optional.of(new RestaurantEntity()));

        RestaurantAlreadyExistException exception = assertThrows(
                RestaurantAlreadyExistException.class,
                () -> restaurantAdapter.saveRestaurant(restaurant, user)
        );
        assertEquals("Restaurant already exists", exception.getMessage());
        verify(restaurantRepository, times(1)).findById(1L);
    }

    @Test
    void saveRestaurant_InvalidUserRole() {

        User user = new User(1L, "user", 3265326, "+573226094632", LocalDate.of(1995, 8, 20), "aa@aa.com", "12334", "CLIENT");
        Restaurant restaurant = new Restaurant(1L, "name", 213123, "rewarded", "+573005698325", "", user, null);

        when(restaurantRepository.findById(1L)).thenReturn(Optional.empty());

        InvalidUserRoleException exception = assertThrows(
                InvalidUserRoleException.class,
                () -> restaurantAdapter.saveRestaurant(restaurant, user)
        );
        assertEquals("User must have the 'Owner' role to create a restaurant", exception.getMessage());
        verify(restaurantRepository, times(1)).findById(1L);

    }

    @Test
    void saveRestaurant_InvalidNitFormat() {

        User user = new User(1L, "user", 3265326, "+573226094632", LocalDate.of(1995, 8, 20), "aa@aa.com", "12334", "OWNER");
        Restaurant restaurant = new Restaurant(1L, "name", -213123, "rewrite", "+573005698325", "", user, null);

        InvalidNitFormatException exception = assertThrows(
                InvalidNitFormatException.class,
                () -> restaurantAdapter.saveRestaurant(restaurant, user)
        );
        assertEquals("Invalid NIT format", exception.getMessage());
        verify(restaurantRepository, times(1)).findById(1L);
    }

    @Test
    void saveRestaurant_InvalidPhoneNumberFormat() {

        LocalDate birthdate = LocalDate.of(1995, 8, 20);
        User user = new User(1L, "user", 3265326, "+573226094632", birthdate, "aa@aa.com", "12334", "OWNER");
        Restaurant restaurant = new Restaurant(1L, "name", 213123, "rewrite", "8325", "", user, null);

        InvalidPhoneNumberException exception = assertThrows(InvalidPhoneNumberException.class, () -> restaurantAdapter.saveRestaurant(restaurant, user));
        assertEquals("Invalid restaurant phone format", exception.getMessage());
        verify(restaurantRepository, times(1)).findById(1L);
    }

    @Test
    void saveRestaurant_InvalidRestaurantName() {

        User user = new User(1L, "aaa", 3265326, "+573226094632", LocalDate.of(1995, 8, 20), "aa@aa.com", "12334", "OWNER");
        Restaurant restaurant = new Restaurant(1L, "12332", 213123, "rewrite", "+573005698325", "", user, null);

        InvalidRestaurantNameException exception = assertThrows(InvalidRestaurantNameException.class, () -> restaurantAdapter.saveRestaurant(restaurant, user));
        assertEquals("Restaurant name cannot consist of only numbers", exception.getMessage());
        verify(restaurantRepository, times(1)).findById(1L);
    }
}