package org.pragma.restaurantplaza.infrastructure.input.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.pragma.restaurantplaza.application.dto.RestaurantRequest;
import org.pragma.restaurantplaza.application.dto.UserRequest;
import org.pragma.restaurantplaza.application.dto.UserResponse;
import org.pragma.restaurantplaza.application.handler.RestaurantHandler;
import org.pragma.restaurantplaza.application.handler.UserHandler;
import org.pragma.restaurantplaza.domain.model.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/admin")
@Tag(name = "Admin", description = "Admin operations")
@RequiredArgsConstructor
public class AdminRestController {

    private final UserHandler userHandler;

    private final RestaurantHandler restaurantHandler;


    @PostMapping("/save_admin")
    @Operation(summary = "Create a new admin",
            description = "This endpoint allows an admin to create a new owner.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Admin created successfully"),
                    @ApiResponse(responseCode = "403", description = "Access denied"),
                    @ApiResponse(responseCode = "400", description = "Bad request", content = @Content)
            })
    public ResponseEntity<String> saveAdmin(@Valid @RequestBody UserRequest userRequest) {
        userRequest.setRole("ADMIN");
        userHandler.saveUser(userRequest);
        return ResponseEntity.ok("Admin created successfully");
    }

    @GetMapping("/get_user/{id}")
    @Operation(summary = "Get a user by ID",
            description = "This endpoint allows an admin to retrieve a user by their ID.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "User retrieved successfully"),
                    @ApiResponse(responseCode = "403", description = "Access denied"),
                    @ApiResponse(responseCode = "404", description = "User not found"),
            })
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long id) {
        UserResponse user = userHandler.getUserById(id);
        if (user != null) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    @GetMapping("/get_users")
    @Operation(summary = "Get all users",
            description = "This endpoint allows an admin to retrieve all users.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Users retrieved successfully"),
                    @ApiResponse(responseCode = "403", description = "Access denied"),
            })
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        List<UserResponse> users = userHandler.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @DeleteMapping("/delete_user/{userId}")
    @Operation(summary = "Delete a user by ID",
            description = "This endpoint allows an admin to delete a user by their ID.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "User deleted successfully"),
                    @ApiResponse(responseCode = "403", description = "Access denied"),
                    @ApiResponse(responseCode = "404", description = "User not found"),
            })
    public ResponseEntity<String> deleteUser(@PathVariable Long userId) {
        boolean deleted = userHandler.deleteUserById(userId);
        if (deleted) {
            return ResponseEntity.ok("User deleted successfully");
        } else {
            return ResponseEntity.ok("User not found");
        }
    }


















    @PostMapping("/save_Owner")
    @Operation(summary = "Create a new owner",
            description = "This endpoint allows an admin to create a new owner.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Owner created successfully"),
                    @ApiResponse(responseCode = "403", description = "Access denied"),
                    @ApiResponse(responseCode = "400", description = "Bad request", content = @Content)
            })
    public ResponseEntity<String> saveOwner(@Valid @RequestBody UserRequest userRequest) {
        userRequest.setRole("OWNER");
        userHandler.saveUser(userRequest);
        return ResponseEntity.ok("Owner created successfully");
    }

    @PostMapping("/save_restaurant")
    @Operation(summary = "Create a new restaurant",
            description = "This endpoint allows an admin to create a new restaurant.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Restaurant created successfully"),
                    @ApiResponse(responseCode = "403", description = "Access denied"),
                    @ApiResponse(responseCode = "400", description = "Bad request", content = @Content)
            })
    public ResponseEntity<String> saveRestaurant(@Valid @RequestBody RestaurantRequest restaurantRequest) {
            restaurantHandler.saveRestaurant(restaurantRequest);
            return ResponseEntity.ok("Restaurant created successfully");
    }





}
