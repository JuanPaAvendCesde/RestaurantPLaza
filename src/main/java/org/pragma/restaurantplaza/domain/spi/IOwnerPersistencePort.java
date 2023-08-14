package org.pragma.restaurantplaza.domain.spi;

import org.pragma.restaurantplaza.domain.model.Owner;

import java.util.List;

public interface IOwnerPersistencePort {


    void saveOwner(Owner owner);

    List<Owner> getAllOwners();

    Owner findById(Owner ownerId);
}
