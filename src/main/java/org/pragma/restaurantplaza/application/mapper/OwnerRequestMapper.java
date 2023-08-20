package org.pragma.restaurantplaza.application.mapper;

import org.pragma.restaurantplaza.application.dto.UserRequest;
import org.pragma.restaurantplaza.domain.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface OwnerRequestMapper {
    User toOwner(UserRequest userRequest);


}
