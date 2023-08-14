package org.pragma.restaurantplaza.application.mapper;

import org.mapstruct.Mapper;
import org.pragma.restaurantplaza.application.dto.OwnerRequest;
import org.pragma.restaurantplaza.domain.model.Owner;

@Mapper(componentModel = "spring",
unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE,
        unmappedSourcePolicy = org.mapstruct.ReportingPolicy.IGNORE )
public interface RestaurantResponseMapper {

    Owner toOwner(OwnerRequest ownerRequest);

}
