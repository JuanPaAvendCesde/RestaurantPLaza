package org.pragma.restaurantplaza.domain.spi;
import org.pragma.restaurantplaza.application.dto.OrderRequest;
import org.pragma.restaurantplaza.application.dto.OrderResponse;
import org.pragma.restaurantplaza.application.dto.UserResponse;


import java.util.List;

public interface IOrderPersistencePort {
    void createOrder(OrderRequest orderRequest);

    void markOrderAsDelivered(String providedPin, Long orderId);

    void cancelOrder(OrderRequest orderRequest);

    void createStatusLog(OrderRequest orderRequest);

    List<OrderResponse> calculateOrderEfficiency();

    List<UserResponse> calculateEmployeeEfficiency();

}
