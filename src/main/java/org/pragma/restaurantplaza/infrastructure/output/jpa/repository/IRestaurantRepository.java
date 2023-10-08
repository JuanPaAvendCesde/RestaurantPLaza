package org.pragma.restaurantplaza.infrastructure.output.jpa.repository;

import org.pragma.restaurantplaza.application.dto.RestaurantResponse;
import org.pragma.restaurantplaza.infrastructure.output.jpa.entity.RestaurantEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface IRestaurantRepository extends JpaRepository<RestaurantEntity, Long> {
    List<RestaurantEntity> findAll();



    Page<RestaurantEntity> findAllByOrderByNameAsc(Pageable pageable);
}
