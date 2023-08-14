package com.example.RestaurantPLaza.domain.api;

import com.example.RestaurantPLaza.domain.model.Owner;

import java.util.List;

public interface IOwnerServicePort {

    void saveOwner(Owner owner);

    List<Owner> getAllOwners();
}
