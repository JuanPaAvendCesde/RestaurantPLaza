package org.pragma.restaurantplaza.infrastructure.output.jpa.mapper;

import org.mapstruct.Mapper;
import org.pragma.restaurantplaza.domain.model.Restaurant;
import org.pragma.restaurantplaza.domain.model.User;
import org.pragma.restaurantplaza.infrastructure.output.jpa.entity.UserEntity;

import java.util.List;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE,
        unmappedSourcePolicy = org.mapstruct.ReportingPolicy.IGNORE)

public interface UserEntityMapper {
    UserEntity toUserEntity(User user);

    User toUser(UserEntity userEntity);

    List<User> toUserList(List<UserEntity> userEntityList);

    Restaurant toRestaurant(UserEntity userEntity);
}
