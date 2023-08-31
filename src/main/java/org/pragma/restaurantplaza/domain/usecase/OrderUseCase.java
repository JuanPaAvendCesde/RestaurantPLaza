package org.pragma.restaurantplaza.domain.usecase;

import org.pragma.restaurantplaza.application.dto.OrderRequest;
import org.pragma.restaurantplaza.domain.api.IOrderServicePort;
import org.pragma.restaurantplaza.domain.spi.IOrderPersistencePort;

public class OrderUseCase implements IOrderServicePort {

    private final IOrderPersistencePort orderPersistencePort;

    public OrderUseCase(IOrderPersistencePort orderPersistencePort) {
        this.orderPersistencePort = orderPersistencePort;
    }

    @Override
    public void createOrder(OrderRequest orderRequest) {
        orderPersistencePort.createOrder(orderRequest);
        
    }
}
