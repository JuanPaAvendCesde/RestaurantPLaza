package org.pragma.restaurantplaza.infrastructure.input.rest;

import lombok.RequiredArgsConstructor;
import org.pragma.restaurantplaza.application.dto.MealRequest;
import org.pragma.restaurantplaza.application.dto.UserRequest;
import org.pragma.restaurantplaza.application.handler.MealHandler;
import org.pragma.restaurantplaza.application.handler.UserHandler;
import org.pragma.restaurantplaza.infrastructure.exception.InvalidUserRoleException;
import org.pragma.restaurantplaza.infrastructure.exception.MealNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/owner")
@RequiredArgsConstructor

public class OwnerRestController {
    private final MealHandler mealHandler;
    private final UserHandler userHandler;

    @PostMapping("/save")
    @PreAuthorize("hasRole('OWNER')and #restaurantOwnerId == authentication.principal.id")
    public ResponseEntity<String> saveMeal(@RequestBody MealRequest mealRequest, Authentication authentication) {
        UserRequest user = (UserRequest) authentication.getPrincipal();
        mealHandler.saveMeal(mealRequest, user);
        return ResponseEntity.ok("Meal created successfully");
    }

    @PutMapping("/update/{mealId}")
    @PreAuthorize("hasRole('Owner') and #restaurantOwnerId == authentication.principal.id")

    public ResponseEntity<String> updateMeal(@PathVariable Long mealId,
                                             @RequestParam int newPrice,
                                             @RequestParam String newDescription) {
        try {
            mealHandler.updateMeal(mealId, newPrice, newDescription);
            return ResponseEntity.ok("Meal updated successfully");
        } catch (InvalidUserRoleException e) {
            return ResponseEntity.badRequest().body("User must have the 'Owner' role to update a meal");
        }
    }

    @PutMapping("/saveEmployee")
    @PreAuthorize("hasRole('OWNER') and #restaurantOwnerId == authentication.principal.id")

    public ResponseEntity<String> saveEmployee(@RequestBody UserRequest userRequest, Authentication authentication) {
        UserRequest user = (UserRequest) authentication.getPrincipal();
        user.setRole("EMPLOYEE");
        userHandler.saveUser(userRequest);
        return ResponseEntity.ok("Employee created successfully");
    }

    @PostMapping("/{mealId}/changeStatus")
    @PreAuthorize("hasRole('OWNER') and #restaurantOwnerId == authentication.principal.id")
    public ResponseEntity<String> changeMealStatus(
            @RequestParam boolean active) {
        try {
            mealHandler.changeMealStatus(active);
            return ResponseEntity.ok("Meal status changed successfully");
        } catch (MealNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (InvalidUserRoleException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid user role");
        }
    }

}
