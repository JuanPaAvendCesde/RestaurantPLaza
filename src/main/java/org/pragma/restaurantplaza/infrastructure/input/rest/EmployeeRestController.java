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
import org.pragma.restaurantplaza.infrastructure.exception.EntityNotFoundException;
import org.pragma.restaurantplaza.infrastructure.exception.InvalidStateException;
import org.pragma.restaurantplaza.infrastructure.output.jpa.adapter.OrderAdapter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

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

   
  /*  @GetMapping("/assigned_order")

    public ResponseEntity<Page<OrderResponse>> getAssignedOrdersByStateAndRestaurant(
            @RequestParam OrderStatus state,
            @RequestParam Long employeeId,
            @RequestParam Long restaurantId,
            @PageableDefault(size = 10) Pageable pageable) {
        Page<OrderResponse> orders = orderHandler.getAssignedOrdersByStateAndRestaurant(state, employeeId, restaurantId, pageable);
        return ResponseEntity.ok(orders);
    }*/


   /* @PostMapping("/{orderId}/markAs_Delivered")

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

   /* @PostMapping("/{orderId}/assign_employee")
    @Operation(summary = "Assign Order to Employee and Change Status",
            description = "Assigns an order to an employee and changes its status to 'IN_PREPARATION'.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Order assigned and status changed successfully"),
                    @ApiResponse(responseCode = "400", description = "Bad request"),
                    @ApiResponse(responseCode = "404", description = "Order or employee not found"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            })
   public ResponseEntity<String> assignOrderToEmployeeAndChangeStatus(
            @PathVariable Long orderId,
            @RequestParam Long employeeId) {
        try {
            orderAdapter.assignOrderToEmployeeAndChangeStatus(orderId, employeeId);
            return ResponseEntity.ok("Order assigned and status changed successfully");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Order or employee not found");
        } catch (InvalidStateException e) {
            return ResponseEntity.badRequest().body("Cannot assign an order that is not pending");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred");
        }
    }*/



}
