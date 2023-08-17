package org.pragma.restaurantplaza.infrastructure.input.rest;

import org.pragma.restaurantplaza.application.dto.UserRequest;
import org.pragma.restaurantplaza.application.dto.RestaurantRequest;
import org.pragma.restaurantplaza.application.handler.UserHandler;
import lombok.RequiredArgsConstructor;
import org.pragma.restaurantplaza.application.handler.RestaurantHandler;
import org.pragma.restaurantplaza.domain.model.User;
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

    private final UserHandler userHandler;

    private final RestaurantHandler restauranteHandler;

    @PostMapping("/saveOwner")
    public ResponseEntity<String>saveOwner (@Valid @RequestBody UserRequest userRequest) {
        userRequest.setRol("User");
        userHandler.saveUser(userRequest);
        return ResponseEntity.ok("User created successfully");
    }

    @PostMapping("/saveRestaurant")
    public ResponseEntity<String> saveRestaurant(@Valid @RequestBody RestaurantRequest restaurantRequest, UserRequest userRequest) {
        // Verificar que el propietario con el id proporcionado exista y tenga el rol de propietario
        User user = userHandler.findById(restaurantRequest.getUserId());
        if (user == null || !"User".equals(user.getRol())) {
            return ResponseEntity.badRequest().body(" is not an user");
        }

        restauranteHandler.saveRestaurant(restaurantRequest, userRequest);
        return ResponseEntity.ok("Restaurant created successfully");
    }
}
