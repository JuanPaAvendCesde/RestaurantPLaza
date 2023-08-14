package com.example.RestaurantPLaza.application.mapper;

import com.example.RestaurantPLaza.application.dto.OwnerResponse;
import com.example.RestaurantPLaza.domain.model.Owner;
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

