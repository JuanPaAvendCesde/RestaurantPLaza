package org.pragma.restaurantplaza.infrastructure.input.rest;

import static org.mockito.Mockito.*;

import org.pragma.restaurantplaza.application.dto.OwnerRequest;
import org.pragma.restaurantplaza.application.handler.OwnerHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

class AdminRestControllerTest {

    private AdminRestController adminRestController;
    private OwnerHandler ownerHandler;

    @BeforeEach
    void setUp() {
        ownerHandler = mock(OwnerHandler.class);
        adminRestController = new AdminRestController(ownerHandler);
    }

    @Test
    void createOwnerWithValidRequest() {

        OwnerRequest ownerRequest = new OwnerRequest();



        ResponseEntity<String> response = adminRestController.saveOwner(ownerRequest);


        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Owner created successfully", response.getBody());
        verify(ownerHandler, times(1)).saveOwner(ownerRequest);
    }
    @Test
    void createOwnerWithInvalidEmail() {
        // Arrange
        OwnerRequest ownerRequest = new OwnerRequest();
        ownerRequest.setName("John");
        ownerRequest.setEmail("invalid-email");


        // Act
        ResponseEntity<String> response = adminRestController.saveOwner(ownerRequest);

        // Assert
        assertEquals(400, response.getStatusCodeValue());
        assertNull(response.getBody());
        verify(ownerHandler, never()).saveOwner(ownerRequest);
    }
    @Test
    void createOwnerWithInvalidPhoneNumber() {
        // Arrange
        OwnerRequest ownerRequest = new OwnerRequest();
        ownerRequest.setName("John");
        ownerRequest.setPhone("+123456789012345"); // Número de teléfono demasiado largo
        ownerRequest.setRol("Owner");

        // Act
        ResponseEntity<String> response = adminRestController.saveOwner(ownerRequest);

        // Assert
        assertEquals(400, response.getStatusCodeValue());
        assertNull(response.getBody());
        verify(ownerHandler, never()).saveOwner(ownerRequest);
    }

    @Test
    void createOwnerWithUnderageUser() {
        // Arrange
        OwnerRequest ownerRequest = new OwnerRequest();
        ownerRequest.setName("John");
        ownerRequest.setBirthdate(LocalDate.now().minusYears(16)); // Menor de 18 años
        ownerRequest.setRol("Owner");

        // Act
        ResponseEntity<String> response = adminRestController.saveOwner(ownerRequest);

        // Assert
        assertEquals(400, response.getStatusCodeValue());
        assertNull(response.getBody());
        verify(ownerHandler, never()).saveOwner(ownerRequest);
    }
}