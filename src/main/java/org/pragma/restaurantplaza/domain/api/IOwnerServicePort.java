package org.pragma.restaurantplaza.domain.api;

import org.pragma.restaurantplaza.domain.model.Owner;

import java.util.List;

public interface IOwnerServicePort {

    void saveOwner(Owner owner);

    List<Owner> getAllOwners();

    Owner findById(Owner ownerId);
}
