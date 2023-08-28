package org.pragma.restaurantplaza.infrastructure.input.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.pragma.restaurantplaza.application.dto.MealResponse;
import org.pragma.restaurantplaza.application.dto.OrderRequest;
import org.pragma.restaurantplaza.application.dto.RestaurantResponse;
import org.pragma.restaurantplaza.application.dto.UserRequest;
import org.pragma.restaurantplaza.application.handler.UserHandler;
import org.pragma.restaurantplaza.domain.model.Restaurant;
import org.pragma.restaurantplaza.infrastructure.exception.EntityNotFoundException;
import org.pragma.restaurantplaza.infrastructure.output.jpa.adapter.OrderAdapter;
import org.pragma.restaurantplaza.infrastructure.output.jpa.adapter.RestaurantAdapter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/client")
@RequiredArgsConstructor
public class ClientRestController {

    private final UserHandler userHandler;
    private final RestaurantAdapter restaurantAdapter;

    private final OrderAdapter orderAdapter;




    @PostMapping("/saveClient")
    @PreAuthorize("hasRole('Client')")
    @Operation(summary = "Save a new client",
            description = "This endpoint allows registration of a new client.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Client created successfully"),
                    @ApiResponse(responseCode = "400", description = "Bad request")
            })
    public ResponseEntity<String> saveClient(@Valid @RequestBody UserRequest userRequest) {
        userRequest.setRole("CLIENT");
        userHandler.saveUser(userRequest);
        return ResponseEntity.ok("Client created successfully");
    }

    @GetMapping("/allRestaurants")
    @Operation(summary = "Get all restaurants",
            description = "Retrieve a paginated list of all restaurants.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successful retrieval of restaurants"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            })
    public Page<RestaurantResponse> getAllRestaurants(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("name"));

        Page<Restaurant> restaurantPage = restaurantAdapter.findAll(pageable);

        return restaurantPage.map(this::mapToRestaurantResponse);
    }

    private RestaurantResponse mapToRestaurantResponse(Restaurant restaurant) {
        return new RestaurantResponse(restaurant.getName(), restaurant.getUrlLogo());
    }




    @GetMapping("/{restaurantId}/menu")
    @Operation(summary = "Get restaurant menu",
            description = "Retrieve a paginated list of meals from a specific restaurant's menu.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successful retrieval of menu items"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            })
    public Page<MealResponse> getRestaurantMenu(
            @PathVariable Long restaurantId,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String category,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return restaurantAdapter.getRestaurantMenuByCategory(restaurantId,name, category, page, size);
    }

    @PostMapping("/create_Order")
    @Operation(summary = "Create a new order",
            description = "Place a new order for a client.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Order created successfully"),
                    @ApiResponse(responseCode = "400", description = "Bad request"),
                    @ApiResponse(responseCode = "404", description = "Client not found"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            })
    public ResponseEntity<String> createOrder(@RequestBody OrderRequest orderRequest) {
        try {
            orderAdapter.createOrder(orderRequest);
            return ResponseEntity.ok("Order created successfully");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Client not found");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred");
        }
    }



}
