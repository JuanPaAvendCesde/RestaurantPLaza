package com.example.RestaurantPLaza.infrastructure.output.jpa.mapper;

import com.example.RestaurantPLaza.domain.model.Owner;
import com.example.RestaurantPLaza.infrastructure.output.jpa.entity.OwnerEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring",
unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE,
        unmappedSourcePolicy = org.mapstruct.ReportingPolicy.IGNORE )

public interface OwnerEntityMapper {
    OwnerEntity toOwnerEntity(Owner owner);

    Owner toOwner(OwnerEntity ownerEntity);

    List<Owner> toOwnerList(List<OwnerEntity> ownerEntityList);
}
