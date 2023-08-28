package org.pragma.restaurantplaza.infrastructure.input.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Owner", description = "Owner operations")
@RequiredArgsConstructor

public class OwnerRestController {
    private final MealHandler mealHandler;
    private final UserHandler userHandler;

    @PostMapping("/save")
    @PreAuthorize("hasRole('OWNER')and #restaurantOwnerId == authentication.principal.id")
    @Operation(summary = "Create a new meal",
            description = "This endpoint allows an owner to create a new meal.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Meal created successfully"),
                    @ApiResponse(responseCode = "403", description = "Access denied"),
                    @ApiResponse(responseCode = "400", description = "Bad request", content = @Content)
            })
    public ResponseEntity<String> saveMeal(@RequestBody MealRequest mealRequest, Authentication authentication) {
        UserRequest user = (UserRequest) authentication.getPrincipal();
        mealHandler.saveMeal(mealRequest, user);
        return ResponseEntity.ok("Meal created successfully");
    }

    @PutMapping("/update/{mealId}")
    @PreAuthorize("hasRole('Owner') and #restaurantOwnerId == authentication.principal.id")
    @Operation(summary = "Update meal information",
            description = "This endpoint allows an owner to update meal information.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Meal updated successfully"),
                    @ApiResponse(responseCode = "403", description = "Access denied"),
                    @ApiResponse(responseCode = "400", description = "Bad request", content = @Content)
            })
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
    @Operation(summary = "Create a new employee",
            description = "This endpoint allows an owner to create a new employee.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Employee created successfully"),
                    @ApiResponse(responseCode = "403", description = "Access denied"),
                    @ApiResponse(responseCode = "400", description = "Bad request", content = @Content)
            })

    public ResponseEntity<String> saveEmployee(@RequestBody UserRequest userRequest, Authentication authentication) {
        UserRequest user = (UserRequest) authentication.getPrincipal();
        user.setRole("EMPLOYEE");
        userHandler.saveUser(userRequest);
        return ResponseEntity.ok("Employee created successfully");
    }

    @PostMapping("/{mealId}/changeStatus")
    @PreAuthorize("hasRole('OWNER') and #restaurantOwnerId == authentication.principal.id")
    @Operation(summary = "Change the status of a meal",
            description = "This endpoint allows an owner to change the status (active/inactive) of a meal.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Meal status changed successfully"),
                    @ApiResponse(responseCode = "403", description = "Access denied"),
                    @ApiResponse(responseCode = "404", description = "Meal not found"),
                    @ApiResponse(responseCode = "403", description = "Invalid user role", content = @Content)
            })
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
