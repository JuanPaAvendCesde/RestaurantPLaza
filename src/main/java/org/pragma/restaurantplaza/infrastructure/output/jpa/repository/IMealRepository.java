package org.pragma.restaurantplaza.infrastructure.output.jpa.repository;

import org.pragma.restaurantplaza.infrastructure.output.jpa.entity.MealEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IMealRepository extends JpaRepository<MealEntity, Long> {




}
