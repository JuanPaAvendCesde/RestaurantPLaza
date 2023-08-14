package org.pragma.restaurantplaza.application.mapper;

import org.pragma.restaurantplaza.application.dto.OwnerResponse;
import org.pragma.restaurantplaza.domain.model.Owner;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring",
unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)

public interface OwnerResponseMapper {
OwnerResponse toOwnerResponse(Owner owner);

    default List<OwnerResponse> toResponseList(List<Owner> ownerList) {
        return ownerList.stream()
                .map(owner -> {
                    OwnerResponse ownerResponse = new OwnerResponse();
                    ownerResponse.setRol(owner.getRol());
                    ownerResponse.setName(owner.getName());

                    return ownerResponse;
                }).toList();
    }
}

