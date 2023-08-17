package org.pragma.restaurantplaza.infrastructure.output.jpa.mapper;

import org.pragma.restaurantplaza.domain.model.User;
import org.pragma.restaurantplaza.infrastructure.output.jpa.entity.UserEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring",
unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE,
        unmappedSourcePolicy = org.mapstruct.ReportingPolicy.IGNORE )

public interface UserEntityMapper {
    UserEntity toOwnerEntity(User user);
    User toOwner(UserEntity userEntity);
    List<User> toOwnerList(List<UserEntity> userEntityList);
}
