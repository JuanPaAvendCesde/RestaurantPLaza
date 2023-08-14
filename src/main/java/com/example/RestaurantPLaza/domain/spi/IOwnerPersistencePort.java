package com.example.RestaurantPLaza.domain.spi;

import com.example.RestaurantPLaza.domain.model.Owner;

import java.util.List;

public interface IOwnerPersistencePort {


    void saveOwner(Owner owner);

    List<Owner> getAllOwners();
}
