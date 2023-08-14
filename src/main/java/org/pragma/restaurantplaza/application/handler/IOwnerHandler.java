package org.pragma.restaurantplaza.application.handler;

import org.pragma.restaurantplaza.application.dto.OwnerRequest;
import org.pragma.restaurantplaza.application.dto.OwnerResponse;

import java.util.List;

public interface IOwnerHandler {

    void saveOwner(OwnerRequest ownerRequest);
    List<OwnerResponse> getAllOwners();


}
