package org.pragma.restaurantplaza.application.handler;

import org.pragma.restaurantplaza.application.dto.OwnerRequest;
import org.pragma.restaurantplaza.application.dto.OwnerResponse;
import org.pragma.restaurantplaza.application.mapper.OwnerRequestMapper;
import org.pragma.restaurantplaza.application.mapper.OwnerResponseMapper;
import org.pragma.restaurantplaza.domain.api.IOwnerServicePort;
import org.pragma.restaurantplaza.domain.model.Owner;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Service
@RequiredArgsConstructor
@Transactional
public class OwnerHandler implements IOwnerHandler{

    private final IOwnerServicePort ownerServicePort;
    private final OwnerRequestMapper ownerRequestMapper;
    private final OwnerResponseMapper ownerResponseMapper;
    @Override
    public void saveOwner(OwnerRequest ownerRequest) {
        Owner owner = ownerRequestMapper.toOwner(ownerRequest);
        ownerServicePort.saveOwner(owner);

    }
    @Override
    public List<OwnerResponse> getAllOwners() {
        return ownerResponseMapper.toResponseList(ownerServicePort.getAllOwners());
    }

    @Override
    public Owner findById(Owner ownerId) {
        return ownerServicePort.findById(ownerId);
    }

}
