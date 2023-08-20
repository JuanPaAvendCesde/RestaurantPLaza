package org.pragma.restaurantplaza.infrastructure.input.rest;

import lombok.RequiredArgsConstructor;
import org.pragma.restaurantplaza.application.dto.MealRequest;
import org.pragma.restaurantplaza.application.handler.MealHandler;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/owner")
@RequiredArgsConstructor
public class OwnerRestController {
    private final MealHandler mealHandler;



    @PostMapping("/saveMeal")
    public ResponseEntity<String> saveMeal(@RequestBody MealRequest mealRequest) {
        mealHandler.saveMeal(mealRequest);
        return ResponseEntity.ok("Meal created successfully");
    }

    @PutMapping("/updateMeal/{mealId}")
    public ResponseEntity<String> updateMeal(@PathVariable Long mealId,
                                             @RequestParam int newPrice,
                                             @RequestParam String newDescription) {
        mealHandler.updateMeal(mealId, newPrice, newDescription);
        return ResponseEntity.ok("Meal updated successfully");
    }




}
