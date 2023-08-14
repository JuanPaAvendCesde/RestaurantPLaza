package org.pragma.restaurantplaza.infrastructure.output.jpa.adapter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.pragma.restaurantplaza.domain.model.Restaurant;
import org.pragma.restaurantplaza.infrastructure.exception.RestaurantAlreadyExistException;
import org.pragma.restaurantplaza.infrastructure.output.jpa.entity.RestaurantEntity;
import org.pragma.restaurantplaza.infrastructure.output.jpa.mapper.RestaurantEntityMapper;
import org.pragma.restaurantplaza.infrastructure.output.jpa.repository.IRestaurantRepository;
import org.pragma.restaurantplaza.infrastructure.output.jpa.adapter.RestaurantAdapter;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RestaurantAdapterTest {

    @Mock
    private IRestaurantRepository restaurantRepository;

    @Mock
    private RestaurantEntityMapper restaurantEntityMapper;

    private RestaurantAdapter restaurantAdapter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        restaurantAdapter = new RestaurantAdapter(restaurantRepository, restaurantEntityMapper);
    }

    @Test
    void saveRestaurant_SuccessfulCreation() {
        // Arrange
        Restaurant restaurant = new Restaurant();
        restaurant.setId(1L);

        when(restaurantRepository.findById(1L)).thenReturn(Optional.empty());
        when(restaurantEntityMapper.toRestaurantEntity(restaurant)).thenReturn(new RestaurantEntity());

        // Act & Assert
        assertDoesNotThrow(() -> restaurantAdapter.saveRestaurant(restaurant));
        verify(restaurantRepository, times(1)).findById(1L);
        verify(restaurantEntityMapper, times(1)).toRestaurantEntity(restaurant);
        verify(restaurantRepository, times(1)).save(any(RestaurantEntity.class));
    }

    @Test
    void saveRestaurant_RestaurantAlreadyExists() {
        // Arrange
        Restaurant restaurant = new Restaurant();
        restaurant.setId(1L);

        when(restaurantRepository.findById(1L)).thenReturn(Optional.of(new RestaurantEntity()));

        // Act & Assert
        RestaurantAlreadyExistException exception = assertThrows(
                RestaurantAlreadyExistException.class,
                () -> restaurantAdapter.saveRestaurant(restaurant)
        );
        assertEquals("Restaurant already exists", exception.getMessage());
        verify(restaurantRepository, times(1)).findById(1L);
        verifyNoMoreInteractions(restaurantEntityMapper);
        verifyNoMoreInteractions(restaurantRepository);
    }
}