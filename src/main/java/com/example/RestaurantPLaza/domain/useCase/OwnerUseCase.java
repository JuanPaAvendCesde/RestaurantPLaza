package com.example.RestaurantPLaza.domain.useCase;

import com.example.RestaurantPLaza.domain.api.IOwnerServicePort;
import com.example.RestaurantPLaza.domain.model.Owner;
import com.example.RestaurantPLaza.domain.spi.IOwnerPersistencePort;

import java.util.List;

public class OwnerUseCase implements IOwnerServicePort {

    private final IOwnerPersistencePort ownerPersistencePort;

    public OwnerUseCase(IOwnerPersistencePort ownerPersistencePort) {
        this.ownerPersistencePort = ownerPersistencePort;
    }

    @Override
    public void saveOwner(Owner owner) {
    ownerPersistencePort.saveOwner(owner);
    }

    @Override
    public List<Owner> getAllOwners() {
        return ownerPersistencePort.getAllOwners();
    }
}
