package com.example.RestaurantPLaza.application.mapper;

import com.example.RestaurantPLaza.application.dto.OwnerRequest;
import com.example.RestaurantPLaza.domain.model.Owner;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface OwnerRequestMapper {
    Owner toOwner(OwnerRequest ownerRequest);


}
