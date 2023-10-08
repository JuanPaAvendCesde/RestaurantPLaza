package org.pragma.restaurantplaza.application.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.pragma.restaurantplaza.application.dto.UserRequest;
import org.pragma.restaurantplaza.application.dto.UserResponse;
import org.pragma.restaurantplaza.domain.model.User;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface UserRequestMapper {
    User toOwner(UserRequest userRequest);


    UserResponse toUserResponse(User user);
}
