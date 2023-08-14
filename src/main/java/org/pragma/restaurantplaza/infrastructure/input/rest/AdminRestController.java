package org.pragma.restaurantplaza.infrastructure.input.rest;

import org.pragma.restaurantplaza.application.dto.OwnerRequest;
import org.pragma.restaurantplaza.application.dto.RestaurantRequest;
import org.pragma.restaurantplaza.application.handler.OwnerHandler;
import lombok.RequiredArgsConstructor;
import org.pragma.restaurantplaza.application.handler.RestaurantHandler;
import org.pragma.restaurantplaza.domain.model.Owner;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminRestController {

    private final OwnerHandler ownerHandler;

    private final RestaurantHandler restauranteHandler;

    @PostMapping("/saveOwner")
    public ResponseEntity<String>saveOwner (@Valid @RequestBody OwnerRequest ownerRequest) {
        ownerRequest.setRol("Owner");
        ownerHandler.saveOwner(ownerRequest);
        return ResponseEntity.ok("Owner created successfully");
    }

    @PostMapping("/saveRestaurant")
    public ResponseEntity<String> saveRestaurant(@Valid @RequestBody RestaurantRequest restaurantRequest,OwnerRequest ownerRequest) {
        // Verificar que el propietario con el id proporcionado exista y tenga el rol de propietario
        Owner owner = ownerHandler.findById(restaurantRequest.getOwnerId());
        if (owner == null || !"Owner".equals(owner.getRol())) {
            return ResponseEntity.badRequest().body(" is not an owner");
        }

        restauranteHandler.saveRestaurant(restaurantRequest, ownerRequest);
        return ResponseEntity.ok("Restaurante creado exitosamente");
    }
}
