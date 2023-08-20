package org.pragma.restaurantplaza.domain.usecase;

import org.pragma.restaurantplaza.domain.api.IOwnerServicePort;
import org.pragma.restaurantplaza.domain.model.Owner;
import org.pragma.restaurantplaza.domain.spi.IOwnerPersistencePort;

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

    @Override
    public Owner findById(Owner ownerId) {
        return ownerPersistencePort.findById(ownerId);
    }
}
