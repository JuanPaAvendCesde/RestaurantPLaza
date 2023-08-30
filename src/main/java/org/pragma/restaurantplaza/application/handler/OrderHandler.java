package org.pragma.restaurantplaza.application.handler;

import lombok.RequiredArgsConstructor;
import org.pragma.restaurantplaza.application.dto.OrderResponse;
import org.pragma.restaurantplaza.domain.model.Order;
import org.pragma.restaurantplaza.domain.model.OrderStatus;
import org.pragma.restaurantplaza.domain.model.Restaurant;
import org.pragma.restaurantplaza.infrastructure.exception.EntityNotFoundException;
import org.pragma.restaurantplaza.infrastructure.output.jpa.entity.MealEntity;
import org.pragma.restaurantplaza.infrastructure.output.jpa.entity.RestaurantEntity;
import org.pragma.restaurantplaza.infrastructure.output.jpa.entity.UserEntity;
import org.pragma.restaurantplaza.infrastructure.output.jpa.repository.IOrderRepository;
import org.pragma.restaurantplaza.infrastructure.output.jpa.repository.IRestaurantRepository;
import org.pragma.restaurantplaza.infrastructure.output.jpa.repository.IUserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderHandler implements IOrderHandler{
    private final IOrderRepository orderRepository;
    private final IUserRepository userRepository;
    private final IRestaurantRepository restaurantRepository;

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
        List<Long> mealIds = new ArrayList<>();
        for (MealEntity mealEntity : order.getMeals(mealIds)) {
            Long id = mealEntity.getId();
            mealIds.add(id);
        }
        Long assignedEmployeeId = order.getAssignedEmployeeId();

        return new OrderResponse(
                order.getId(),
                order.getUser(),
                order.getRestaurant(),
                order.getMeals(mealIds),
                order.getOrderStatus(),
                assignedEmployeeId,
                order.getQuantity(),
                order.getSecurityPin()
        );
    }



    public Page<OrderResponse> getAssignedOrdersByStateAndRestaurant(
            OrderStatus state, Long employeeId, Long restaurantId, Pageable pageable) {
        UserEntity user = userRepository.findById(employeeId)
                .orElseThrow(() -> new EntityNotFoundException("Employee not found"));

        RestaurantEntity restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new EntityNotFoundException("Restaurant not found"));

        return orderRepository.findByOrderStatusAndAssignedEmployeeIdAndRestaurant(state, employeeId, restaurant, pageable)
                .map(order -> mapToOrderResponse(order));
    }
}
