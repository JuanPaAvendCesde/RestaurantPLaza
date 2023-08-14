package com.example.RestaurantPLaza.infrastructure.output.jpa.repository;

import com.example.RestaurantPLaza.infrastructure.output.jpa.entity.OwnerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IOwnerRepository extends JpaRepository<OwnerEntity, Long> {



}
