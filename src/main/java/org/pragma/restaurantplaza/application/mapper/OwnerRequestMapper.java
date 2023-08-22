package org.pragma.restaurantplaza.application.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.pragma.restaurantplaza.application.dto.UserRequest;
import org.pragma.restaurantplaza.domain.model.User;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface OwnerRequestMapper {
    User toOwner(UserRequest userRequest);


}
