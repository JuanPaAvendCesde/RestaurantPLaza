package org.pragma.restaurantplaza.infrastructure.output.jpa.mapper;

import org.mapstruct.Mapper;
import org.pragma.restaurantplaza.domain.model.Order;
import org.pragma.restaurantplaza.infrastructure.output.jpa.entity.OrderEntity;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE,
        unmappedSourcePolicy = org.mapstruct.ReportingPolicy.IGNORE)
public interface OrderEntityMapper {
    OrderEntity toOrderEntity(Order order);
}
