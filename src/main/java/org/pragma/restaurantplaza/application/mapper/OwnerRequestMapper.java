package org.pragma.restaurantplaza.application.mapper;

import org.pragma.restaurantplaza.application.dto.OwnerRequest;
import org.pragma.restaurantplaza.domain.model.Owner;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface OwnerRequestMapper {
    Owner toOwner(OwnerRequest ownerRequest);


}
