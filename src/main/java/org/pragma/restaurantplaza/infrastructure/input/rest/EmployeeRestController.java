package org.pragma.restaurantplaza.infrastructure.input.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.pragma.restaurantplaza.application.dto.OrderResponse;
import org.pragma.restaurantplaza.application.dto.UserRequest;
import org.pragma.restaurantplaza.application.handler.OrderHandler;
import org.pragma.restaurantplaza.application.handler.UserHandler;
import org.pragma.restaurantplaza.domain.model.OrderStatus;
import org.pragma.restaurantplaza.domain.model.Restaurant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@RestController
@RequestMapping("/employee")
@RequiredArgsConstructor
@Tag(name = "Employee Operations", description = "API endpoints for employees")
public class EmployeeRestController {

    private final OrderHandler orderHandler;
    private final UserHandler userHandler;

    @GetMapping("/orders_by_state")
    @Operation(summary = "Get orders by state",
            description = "Retrieve a paginated list of orders based on their state, filtered for the current restaurant's employee.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successful retrieval of orders"),
                    @ApiResponse(responseCode = "400", description = "Invalid input parameters"),
                    @ApiResponse(responseCode = "403", description = "Permission denied")
            })
    public ResponseEntity<Page<OrderResponse>> getOrdersByState(
            @RequestParam(required = false) OrderStatus state,
            @RequestParam(defaultValue = "0") @PositiveOrZero int page,
            @RequestParam(defaultValue = "10") @Positive int size,
            Authentication authentication) {

        // Validate employee's access to restaurant
        UserRequest user = (UserRequest) authentication.getPrincipal();
        Restaurant restaurant = userHandler.getEmployeeRestaurant(user.getId());
        if (restaurant == null) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Employee does not belong to any restaurant");
        }

        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<OrderResponse> ordersPage = orderHandler.getOrdersByStateAndRestaurant(state, restaurant, pageable);

        return ResponseEntity.ok(ordersPage);
    }
}
