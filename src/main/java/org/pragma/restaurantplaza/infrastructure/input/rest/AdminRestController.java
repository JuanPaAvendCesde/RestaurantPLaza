package org.pragma.restaurantplaza.infrastructure.input.rest;

import lombok.RequiredArgsConstructor;
import org.pragma.restaurantplaza.application.dto.RestaurantRequest;
import org.pragma.restaurantplaza.application.dto.UserRequest;
import org.pragma.restaurantplaza.application.handler.RestaurantHandler;
import org.pragma.restaurantplaza.application.handler.UserHandler;
import org.pragma.restaurantplaza.domain.model.User;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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

    private final RestaurantHandler restaurantHandler;

    @PostMapping("/saveOwner")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> saveOwner(@Valid @RequestBody UserRequest userRequest) {
        userRequest.setRole("OWNER");
        userHandler.saveUser(userRequest);
        return ResponseEntity.ok("Owner created successfully");
    }

    @PostMapping("/saveRestaurant")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> saveRestaurant(@Valid @RequestBody RestaurantRequest restaurantRequest, @RequestBody UserRequest userRequest) {

        User user = userHandler.findById(restaurantRequest.getUserId());
        if (user == null || !"OWNER".equals(user.getRole())) {
            return ResponseEntity.badRequest().body("User is not an Owner");
        }
        restaurantHandler.saveRestaurant(restaurantRequest, userRequest);
        return ResponseEntity.ok("Restaurant created successfully");
    }
}
