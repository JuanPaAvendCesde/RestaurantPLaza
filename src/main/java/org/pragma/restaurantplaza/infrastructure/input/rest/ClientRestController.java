package org.pragma.restaurantplaza.infrastructure.input.rest;

import lombok.RequiredArgsConstructor;
import org.pragma.restaurantplaza.application.dto.MealResponse;
import org.pragma.restaurantplaza.application.dto.RestaurantResponse;
import org.pragma.restaurantplaza.application.dto.UserRequest;
import org.pragma.restaurantplaza.application.handler.UserHandler;
import org.pragma.restaurantplaza.domain.model.Restaurant;
import org.pragma.restaurantplaza.infrastructure.output.jpa.adapter.RestaurantAdapter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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


    @PostMapping("/saveClient")
    @PreAuthorize("hasRole('Client')")
    public ResponseEntity<String> saveClient(@Valid @RequestBody UserRequest userRequest) {
        userRequest.setRole("CLIENT");
        userHandler.saveUser(userRequest);
        return ResponseEntity.ok("Client created successfully");
    }

    @GetMapping("/allRestaurants")
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
    public Page<MealResponse> getRestaurantMenu(
            @PathVariable Long restaurantId,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String category,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return restaurantAdapter.getRestaurantMenuByCategory(restaurantId,name, category, page, size);
    }

}
