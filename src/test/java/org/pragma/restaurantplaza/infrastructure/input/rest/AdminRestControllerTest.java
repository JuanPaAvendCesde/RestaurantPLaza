package org.pragma.restaurantplaza.infrastructure.input.rest;

import static org.mockito.Mockito.*;

import org.pragma.restaurantplaza.application.dto.UserRequest;
import org.pragma.restaurantplaza.application.handler.UserHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.pragma.restaurantplaza.application.handler.RestaurantHandler;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

class AdminRestControllerTest {

    private AdminRestController adminRestController;
    private UserHandler ownerHandler;
    private RestaurantHandler RestaurantHandler;

    @BeforeEach
    void setUp() {
        ownerHandler = mock(UserHandler.class);
        adminRestController = new AdminRestController(ownerHandler, RestaurantHandler);
    }

    @Test
    void createOwnerWithValidRequest() {

        UserRequest userRequest = new UserRequest();



        ResponseEntity<String> response = adminRestController.saveOwner(userRequest);


        assertEquals(200, response.getStatusCodeValue());
        assertEquals("User created successfully", response.getBody());
        verify(ownerHandler, times(1)).saveUser(userRequest);
    }
    @Test
    void createOwnerWithInvalidEmail() {
        // Arrange
        UserRequest userRequest = new UserRequest();
        userRequest.setName("John");
        userRequest.setEmail("invalid-email");


        // Act
        ResponseEntity<String> response = adminRestController.saveOwner(userRequest);

        // Assert
        assertEquals(400, response.getStatusCodeValue());
        assertNull(response.getBody());
        verify(ownerHandler, never()).saveUser(userRequest);
    }
    @Test
    void createOwnerWithInvalidPhoneNumber() {
        // Arrange
        UserRequest userRequest = new UserRequest();
        userRequest.setName("John");
        userRequest.setPhone("+123456789012345"); // Número de teléfono demasiado largo
        userRequest.setRol("User");

        // Act
        ResponseEntity<String> response = adminRestController.saveOwner(userRequest);

        // Assert
        assertEquals(400, response.getStatusCodeValue());
        assertNull(response.getBody());
        verify(ownerHandler, never()).saveUser(userRequest);
    }

    @Test
    void createOwnerWithUnderageUser() {
        // Arrange
        UserRequest userRequest = new UserRequest();
        userRequest.setName("John");
        userRequest.setBirthdate(LocalDate.now().minusYears(16)); // Menor de 18 años
        userRequest.setRol("User");

        // Act
        ResponseEntity<String> response = adminRestController.saveOwner(userRequest);

        // Assert
        assertEquals(400, response.getStatusCodeValue());
        assertNull(response.getBody());
        verify(ownerHandler, never()).saveUser(userRequest);
    }
}