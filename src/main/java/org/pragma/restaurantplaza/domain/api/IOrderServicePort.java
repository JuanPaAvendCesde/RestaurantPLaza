package org.pragma.restaurantplaza.domain.api;

import org.pragma.restaurantplaza.application.dto.OrderRequest;


public interface IOrderServicePort {

        void createOrder(OrderRequest orderRequest);
}
