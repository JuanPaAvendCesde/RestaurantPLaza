package org.pragma.restaurantplaza.domain.spi;
import org.pragma.restaurantplaza.application.dto.OrderRequest;
import org.pragma.restaurantplaza.application.dto.OrderResponse;
import org.pragma.restaurantplaza.application.dto.UserResponse;
import org.pragma.restaurantplaza.domain.model.Order;
import org.pragma.restaurantplaza.infrastructure.output.jpa.entity.OrderEntity;


import java.util.List;

public interface IOrderPersistencePort {
    OrderEntity createOrder(Long userId, Long restaurantId, List<OrderResponse> orderItems);


  /*  void markOrderAsDelivered(String providedPin, Long orderId);

    void cancelOrder(OrderRequest orderRequest);

    void createStatusLog(OrderRequest orderRequest);

   List<OrderResponse> calculateOrderEfficiency();

    List<UserResponse> calculateEmployeeEfficiency();

    void assignOrderToEmployeeAndChangeStatus(Long orderId, Long employeeId)*/;
}
