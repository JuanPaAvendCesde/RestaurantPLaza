package org.pragma.restaurantplaza.application.handler;

import lombok.RequiredArgsConstructor;
import org.pragma.restaurantplaza.application.dto.OrderResponse;
import org.pragma.restaurantplaza.domain.model.Order;
import org.pragma.restaurantplaza.domain.model.OrderStatus;
import org.pragma.restaurantplaza.domain.model.Restaurant;
import org.pragma.restaurantplaza.infrastructure.output.jpa.repository.IOrderRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderHandler implements IOrderHandler{
    private final IOrderRepository orderRepository;

    @Override
    public Page<OrderResponse> getOrdersByStateAndRestaurant(OrderStatus state, Restaurant restaurant, Pageable pageable) {
        Page<Order> ordersPage = orderRepository.findByOrderStatusAndRestaurant(state, restaurant, pageable);

        // Convertir la lista de entidades Order a objetos OrderResponse
        List<OrderResponse> orderResponses = ordersPage.getContent()
                .stream()
                .map(this::mapToOrderResponse)
                .collect(Collectors.toList());

        // Crear una nueva página de resultados con los objetos OrderResponse
        return new PageImpl<>(orderResponses, pageable, ordersPage.getTotalElements());
    }

    private OrderResponse mapToOrderResponse(Order order) {

        return new OrderResponse(

        );
    }
}
