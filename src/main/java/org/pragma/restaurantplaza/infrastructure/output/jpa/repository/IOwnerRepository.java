package org.pragma.restaurantplaza.infrastructure.output.jpa.repository;

import org.pragma.restaurantplaza.infrastructure.output.jpa.entity.OwnerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IOwnerRepository extends JpaRepository<OwnerEntity, Long> {

    Optional<OwnerEntity> findById(Long ownerId);

}
