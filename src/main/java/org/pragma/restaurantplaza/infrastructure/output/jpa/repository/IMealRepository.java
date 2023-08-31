package org.pragma.restaurantplaza.infrastructure.output.jpa.repository;

import org.pragma.restaurantplaza.infrastructure.output.jpa.entity.MealEntity;
import org.pragma.restaurantplaza.infrastructure.output.jpa.entity.RestaurantEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface IMealRepository extends JpaRepository<MealEntity, Long> {


    Page<MealEntity> findByRestaurantIdAndCategory(RestaurantEntity restaurant, String category, Pageable pageable);

    Page<MealEntity> findByRestaurantId(RestaurantEntity restaurant, Pageable pageable);


}
