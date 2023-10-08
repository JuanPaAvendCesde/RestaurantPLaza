package org.pragma.restaurantplaza.application.handler;

import lombok.RequiredArgsConstructor;
import org.pragma.restaurantplaza.application.dto.OrderResponse;
import org.pragma.restaurantplaza.domain.model.Order;
import org.pragma.restaurantplaza.domain.model.OrderStatus;
import org.pragma.restaurantplaza.domain.model.Restaurant;
import org.pragma.restaurantplaza.infrastructure.exception.EntityNotFoundException;
import org.pragma.restaurantplaza.infrastructure.output.jpa.entity.MealEntity;
import org.pragma.restaurantplaza.infrastructure.output.jpa.entity.RestaurantEntity;
import org.pragma.restaurantplaza.infrastructure.output.jpa.repository.IOrderRepository;
import org.pragma.restaurantplaza.infrastructure.output.jpa.repository.IRestaurantRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
@Transactional
public class OrderHandler implements IOrderHandler{
    private final IOrderRepository orderRepository;
    private final IRestaurantRepository restaurantRepository;

   /* @Override
     public Page<OrderResponse> getOrdersByStateAndRestaurant(OrderStatus state, Restaurant restaurant, Pageable pageable) {
        Page<Order> ordersPage = orderRepository.findByOrderStatusAndRestaurant(state, restaurant, pageable);

        List<OrderResponse> orderResponses = ordersPage.getContent()
                .stream()
                .map(this::mapToOrderResponse)
                .toList();


        return new PageImpl<>(orderResponses, pageable, ordersPage.getTotalElements());
    }

   private OrderResponse mapToOrderResponse(Order order) {
        List<Long> meals = new ArrayList<>();
        for (MealEntity mealEntity : order.getMeals()) {
            Long id = mealEntity.getId();
            meals.add(id);
        }
        Long assignedEmployeeId = order.getAssignedEmployeeId();

        return new OrderResponse(
                order.getId(),
                order.getUser(),
                order.getRestaurant(),
                order.getMeals(),
                order.getOrderStatus(),
                assignedEmployeeId,
                order.getQuantity(),
                order.getSecurityPin(),
                order.getCreateAt(),
                order.getUpdateAt(),
                order.getEstimatedTime()
        );
    }

    public Page<OrderResponse> getAssignedOrdersByStateAndRestaurant(
            OrderStatus state, Long employeeId, Long restaurantId, Pageable pageable) {

        RestaurantEntity restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new EntityNotFoundException("Restaurant not found"));

        return orderRepository.findByOrderStatusAndAssignedEmployeeIdAndRestaurant(state, employeeId, restaurant, pageable)
                .map(order -> mapToOrderResponse(order));
    }*/
}
