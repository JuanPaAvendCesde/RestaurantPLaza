package org.pragma.restaurantplaza.domain.spi;

import org.pragma.restaurantplaza.application.dto.OrderRequest;

public interface IOrderPersistencePort {
    void createOrder(OrderRequest orderRequest);
}
