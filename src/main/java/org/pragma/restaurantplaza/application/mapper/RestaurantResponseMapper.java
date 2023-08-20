package org.pragma.restaurantplaza.application.mapper;

import org.mapstruct.Mapper;
import org.pragma.restaurantplaza.application.dto.UserRequest;
import org.pragma.restaurantplaza.domain.model.User;

@Mapper(componentModel = "spring",
unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE,
        unmappedSourcePolicy = org.mapstruct.ReportingPolicy.IGNORE )
public interface RestaurantResponseMapper {

    User toOwner(UserRequest userRequest);

}
