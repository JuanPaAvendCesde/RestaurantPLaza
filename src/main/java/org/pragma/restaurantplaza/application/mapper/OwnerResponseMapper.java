package org.pragma.restaurantplaza.application.mapper;

import org.pragma.restaurantplaza.application.dto.UserResponse;
import org.pragma.restaurantplaza.domain.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring",
unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)

public interface OwnerResponseMapper {
UserResponse toOwnerResponse(User user);

    default List<UserResponse> toResponseList(List<User> userList) {
        return userList.stream()
                .map(owner -> {
                    UserResponse userResponse = new UserResponse();
                    userResponse.setRol(owner.getRol());
                    userResponse.setName(owner.getName());

                    return userResponse;
                }).toList();
    }
}

