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
import org.pragma.restaurantplaza.infrastructure.exception.InvalidStateException;
import org.pragma.restaurantplaza.infrastructure.output.jpa.adapter.OrderAdapter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
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
    private final OrderAdapter orderAdapter;

    @GetMapping("/orders_by_state")
    @PreAuthorize("hasRole('Employee')")
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


        UserRequest user = (UserRequest) authentication.getPrincipal();
        Restaurant restaurant = userHandler.getEmployeeRestaurant(user.getId());
        if (restaurant == null) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Employee does not belong to any restaurant");
        }

        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<OrderResponse> ordersPage = orderHandler.getOrdersByStateAndRestaurant(state, restaurant, pageable);

        return ResponseEntity.ok(ordersPage);
    }
    @GetMapping("/assigned_order")
    @PreAuthorize("hasRole('Employee')")
    public ResponseEntity<Page<OrderResponse>> getAssignedOrdersByStateAndRestaurant(
            @RequestParam OrderStatus state,
            @RequestParam Long employeeId,
            @RequestParam Long restaurantId,
            @PageableDefault(size = 10) Pageable pageable) {
        Page<OrderResponse> orders = orderHandler.getAssignedOrdersByStateAndRestaurant(state, employeeId, restaurantId, pageable);
        return ResponseEntity.ok(orders);
    }


    @PostMapping("/{orderId}/markAsDelivered")
    @PreAuthorize("hasRole('OWNER')")
    @Operation(summary = "Mark an order as delivered",
            description = "This endpoint allows an owner to mark an order as delivered.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Order marked as delivered"),
                    @ApiResponse(responseCode = "400", description = "Bad request"),
                    @ApiResponse(responseCode = "403", description = "Access denied"),
                    @ApiResponse(responseCode = "404", description = "Order not found"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            })
    public ResponseEntity<String> markOrderAsDelivered(
            @PathVariable Long orderId,
            @RequestParam String providedPin) {
        try {
            orderAdapter.markOrderAsDelivered(providedPin, orderId);
            return ResponseEntity.ok("Order marked as delivered");
        } catch (InvalidStateException e) {
            return ResponseEntity.badRequest().body("Unable to mark order as delivered");
        }
    }



}
