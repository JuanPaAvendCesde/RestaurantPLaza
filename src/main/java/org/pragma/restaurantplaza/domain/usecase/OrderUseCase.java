package org.pragma.restaurantplaza.domain.usecase;

import org.pragma.restaurantplaza.application.dto.OrderRequest;
import org.pragma.restaurantplaza.domain.api.IOrderServicePort;
import org.pragma.restaurantplaza.domain.model.Order;
import org.pragma.restaurantplaza.domain.spi.IOrderPersistencePort;
import org.pragma.restaurantplaza.infrastructure.output.jpa.entity.OrderEntity;

public class OrderUseCase implements IOrderServicePort {

    private final IOrderPersistencePort orderPersistencePort;

    public OrderUseCase(IOrderPersistencePort orderPersistencePort) {
        this.orderPersistencePort = orderPersistencePort;
    }



}
