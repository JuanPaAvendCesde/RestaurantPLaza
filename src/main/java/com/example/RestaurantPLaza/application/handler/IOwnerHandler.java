package com.example.RestaurantPLaza.application.handler;

import com.example.RestaurantPLaza.application.dto.OwnerRequest;
import com.example.RestaurantPLaza.application.dto.OwnerResponse;

import java.util.List;

public interface IOwnerHandler {

    void saveOwner(OwnerRequest ownerRequest);

    List<OwnerResponse> getAllOwners();


}
