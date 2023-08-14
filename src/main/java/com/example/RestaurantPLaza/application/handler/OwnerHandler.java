package com.example.RestaurantPLaza.application.handler;

import com.example.RestaurantPLaza.application.dto.OwnerRequest;
import com.example.RestaurantPLaza.application.dto.OwnerResponse;
import com.example.RestaurantPLaza.application.mapper.OwnerRequestMapper;
import com.example.RestaurantPLaza.application.mapper.OwnerResponseMapper;
import com.example.RestaurantPLaza.domain.api.IOwnerServicePort;
import com.example.RestaurantPLaza.domain.model.Owner;
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
}
