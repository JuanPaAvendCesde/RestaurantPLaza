package org.pragma.restaurantplaza.infrastructure.output.jpa.repository;

import org.pragma.restaurantplaza.domain.model.Order;
import org.pragma.restaurantplaza.domain.model.OrderStatus;
import org.pragma.restaurantplaza.domain.model.Restaurant;
import org.pragma.restaurantplaza.infrastructure.output.jpa.entity.OrderEntity;
import org.pragma.restaurantplaza.infrastructure.output.jpa.entity.RestaurantEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IOrderRepository extends JpaRepository<OrderEntity,Long> {
    Page<Order> findByOrderStatusAndRestaurant(OrderStatus state, Restaurant restaurant, Pageable pageable);

    Page<Order> findByOrderStatusAndAssignedEmployeeIdAndRestaurant(OrderStatus state, Long employeeId, RestaurantEntity restaurant, Pageable pageable);


    List<OrderEntity> findByAssignedEmployeeIdAndOrderStatus(long id, OrderStatus orderStatus);
}
