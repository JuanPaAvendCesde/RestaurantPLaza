package org.pragma.restaurantplaza.infrastructure.input.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.pragma.restaurantplaza.application.dto.MealResponse;
import org.pragma.restaurantplaza.application.dto.OrderRequest;
import org.pragma.restaurantplaza.application.dto.RestaurantResponse;
import org.pragma.restaurantplaza.application.dto.UserRequest;
import org.pragma.restaurantplaza.application.handler.UserHandler;
import org.pragma.restaurantplaza.domain.model.Order;
import org.pragma.restaurantplaza.domain.model.Restaurant;
import org.pragma.restaurantplaza.infrastructure.exception.EntityNotFoundException;
import org.pragma.restaurantplaza.infrastructure.exception.InvalidStateException;
import org.pragma.restaurantplaza.infrastructure.output.jpa.adapter.MealAdapter;
import org.pragma.restaurantplaza.infrastructure.output.jpa.adapter.OrderAdapter;
import org.pragma.restaurantplaza.infrastructure.output.jpa.adapter.RestaurantAdapter;
import org.pragma.restaurantplaza.infrastructure.output.jpa.entity.MealEntity;
import org.pragma.restaurantplaza.infrastructure.output.jpa.entity.RestaurantEntity;
import org.pragma.restaurantplaza.infrastructure.output.jpa.entity.UserEntity;
import org.pragma.restaurantplaza.infrastructure.output.jpa.mapper.UserEntityMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/client")
@RequiredArgsConstructor
public class ClientRestController {

    private final UserHandler userHandler;
    private final RestaurantAdapter restaurantAdapter;
    private final MealAdapter mealAdapter;
    private final UserEntityMapper userEntityMapper;


    @PostMapping("/save_client")
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



    @GetMapping("/list")
    public Page<RestaurantResponse> listRestaurantsOrderedByName(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return restaurantAdapter.getAllRestaurantsOrderedByName(pageable);
    }

    @GetMapping("/menu/{restaurantId}")
    public Page<MealResponse> listMenu(
            @PathVariable Long restaurantId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = true) String category) {
        RestaurantEntity restaurantEntity = restaurantAdapter.findById(restaurantId);
        Pageable pageable = PageRequest.of(page, size);
        return mealAdapter.getMenuByRestaurant(restaurantEntity, category, pageable);
    }







/*

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

    @PostMapping("/create_order")
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



    @PostMapping("/{orderId}/cancel_order")
    @Operation(summary = "Create a new order",
            description = "Place a new order for a client.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Order created successfully"),
                    @ApiResponse(responseCode = "400", description = "Bad Request"),
                    @ApiResponse(responseCode = "404", description = "Client not found"),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error")
            })
    public ResponseEntity<String> cancelOrder(@PathVariable Long orderId,OrderRequest orderRequest) {
        Order order = orderAdapter.getOrderById(orderId);

        orderRequest.setId(order.getId());

        try {
            orderAdapter.cancelOrder(orderRequest);
            return ResponseEntity.ok("Order canceled successfully");
        } catch (InvalidStateException e) {
            return ResponseEntity.badRequest().body("Can't cancel order");
        }
    }

*/

    }




