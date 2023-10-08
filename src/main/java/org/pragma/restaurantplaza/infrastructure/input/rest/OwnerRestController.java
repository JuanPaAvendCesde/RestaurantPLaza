package org.pragma.restaurantplaza.infrastructure.input.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.pragma.restaurantplaza.application.dto.MealRequest;
import org.pragma.restaurantplaza.application.dto.OrderResponse;
import org.pragma.restaurantplaza.application.dto.UserRequest;
import org.pragma.restaurantplaza.application.dto.UserResponse;
import org.pragma.restaurantplaza.application.handler.MealHandler;
import org.pragma.restaurantplaza.application.handler.UserHandler;
import org.pragma.restaurantplaza.domain.model.Meal;
import org.pragma.restaurantplaza.infrastructure.exception.EntityNotFoundException;
import org.pragma.restaurantplaza.infrastructure.exception.InvalidUserRoleException;
import org.pragma.restaurantplaza.infrastructure.exception.MealNotFoundException;
import org.pragma.restaurantplaza.infrastructure.output.jpa.adapter.OrderAdapter;
import org.pragma.restaurantplaza.infrastructure.output.jpa.entity.MealEntity;
import org.pragma.restaurantplaza.infrastructure.output.jpa.mapper.MealEntityMapper;
import org.pragma.restaurantplaza.infrastructure.output.jpa.repository.IMealRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/owner")
@Tag(name = "Owner", description = "Owner operations")
@RequiredArgsConstructor

public class OwnerRestController {
    private final MealHandler mealHandler;
    private final UserHandler userHandler;
    private final OrderAdapter orderAdapter;
    private final MealEntityMapper mealEntityMapper;
    private final IMealRepository mealRepository;

    @PostMapping("/save_meal")

    @Operation(summary = "Create a new meal",
            description = "This endpoint allows an owner to create a new meal.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Meal created successfully"),
                    @ApiResponse(responseCode = "403", description = "Access denied"),
                    @ApiResponse(responseCode = "400", description = "Bad request", content = @Content)
            })
    public ResponseEntity<String> saveMeal(@RequestBody MealRequest mealRequest) {
        try {

            MealEntity mealEntity = mealEntityMapper.toMealEntity(mealRequest);


            mealRepository.save(mealEntity);

            return ResponseEntity.ok("Meal created successfully");
        } catch (Exception e) {

            return ResponseEntity.badRequest().body("Bad request: " + e.getMessage());
        }
    }
    @PutMapping("/update/meal/{id}")
    @Operation(summary = "Update a meal by ID",
            description = "This endpoint allows a restaurant owner to update a meal by its ID.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Meal updated successfully"),
                    @ApiResponse(responseCode = "404", description = "Meal not found"),
                    @ApiResponse(responseCode = "400", description = "Bad request", content = @Content)
            })
    public ResponseEntity<Void> updateMealById(@PathVariable Long id, @RequestBody MealRequest mealRequest) {
        mealRequest.setId(id); // Establecer el ID del plato en función del valor de la ruta
        mealHandler.updateMeal(mealRequest);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/save_employee")
    @Operation(summary = "Create a new employee",
            description = "This endpoint allows an owner to create a new employee.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Employee created successfully"),
                    @ApiResponse(responseCode = "403", description = "Access denied"),
                    @ApiResponse(responseCode = "400", description = "Bad request", content = @Content)
            })

    public ResponseEntity<String> saveEmployee(@RequestBody UserRequest userRequest) {

        userRequest.setRole("EMPLOYEE");
        userHandler.saveUser(userRequest);
        return ResponseEntity.ok("Employee created successfully");
    }

    @PutMapping("/changeStatus/{mealId}")
    @Operation(summary = "Change the status of a meal",
            description = "This endpoint allows an owner to change the status (active/inactive) of a meal by its ID.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Meal status changed successfully"),
                    @ApiResponse(responseCode = "403", description = "Access denied"),
                    @ApiResponse(responseCode = "404", description = "Meal not found"),
                    @ApiResponse(responseCode = "403", description = "Invalid user role", content = @Content)
            })
    public ResponseEntity<String> changeMealStatus(
            @PathVariable Long mealId,
            @RequestParam boolean active) {
        try {
            mealHandler.changeMealStatusById(mealId, active); // Pasar el ID del plato y el estado activo
            return ResponseEntity.ok("Meal status changed successfully");
        } catch (MealNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (InvalidUserRoleException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid user role");
        }
    }
   /* @GetMapping("/get_orderEfficiency")

    @Operation(summary = "Get order efficiency",
            description = "Retrieve order efficiency statistics.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successful retrieval of order efficiency"),
                    @ApiResponse(responseCode = "403", description = "Access denied"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            })
    public ResponseEntity<List<OrderResponse>> getOrderEfficiency() {
        List<OrderResponse> orderEfficiencyList = orderAdapter.calculateOrderEfficiency();
        return ResponseEntity.ok(orderEfficiencyList);
    }

    @GetMapping("/get_employeeEfficiency")

    @Operation(summary = "Get employee efficiency",
            description = "Retrieve employee efficiency statistics.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successful retrieval of employee efficiency"),
                    @ApiResponse(responseCode = "403", description = "Access denied"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            })
    public ResponseEntity<List<UserResponse>> getEmployeeEfficiency() {
        List<UserResponse> employeeEfficiencyList = orderAdapter.calculateEmployeeEfficiency();
        return ResponseEntity.ok(employeeEfficiencyList);
    }*/

}
