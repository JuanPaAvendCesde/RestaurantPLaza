package org.pragma.restaurantplaza.infrastructure.output.jpa.adapter;

import lombok.RequiredArgsConstructor;
import org.pragma.restaurantplaza.application.dto.OrderRequest;
import org.pragma.restaurantplaza.domain.model.Meal;
import org.pragma.restaurantplaza.domain.model.Order;
import org.pragma.restaurantplaza.domain.model.OrderStatus;
import org.pragma.restaurantplaza.domain.model.User;
import org.pragma.restaurantplaza.domain.spi.IOrderPersistencePort;
import org.pragma.restaurantplaza.infrastructure.exception.EntityNotFoundException;
import org.pragma.restaurantplaza.infrastructure.output.jpa.entity.MealEntity;
import org.pragma.restaurantplaza.infrastructure.output.jpa.entity.UserEntity;
import org.pragma.restaurantplaza.infrastructure.output.jpa.mapper.MealEntityMapper;
import org.pragma.restaurantplaza.infrastructure.output.jpa.mapper.OrderEntityMapper;
import org.pragma.restaurantplaza.infrastructure.output.jpa.repository.IMealRepository;
import org.pragma.restaurantplaza.infrastructure.output.jpa.repository.IOrderRepository;
import org.pragma.restaurantplaza.infrastructure.output.jpa.repository.IUserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
@Service
@RequiredArgsConstructor
public class OrderAdapter implements IOrderPersistencePort {
    private final IOrderRepository orderRepository;
    private final OrderEntityMapper orderEntityMapper;
    private final IUserRepository userRepository;
    private final IMealRepository mealRepository;
    private final MealEntityMapper mealEntityMapper;
    private List<Meal> selectedMeals;

    @Override
    public void createOrder(OrderRequest orderRequest) {
        // Obtener el cliente que realiza el pedido
        UserEntity client = userRepository.findById(orderRequest.getId())
                .orElseThrow(() -> new EntityNotFoundException("Client not found"));

        // Obtener el menú de un restaurante específico (usando el restaurante en orderRequest)
        List<MealEntity> selectedMealEntities = selectedMeals.stream()
                .map(mealEntityMapper::toMealEntity)
                .collect(Collectors.toList());
        Order order = getOrder(orderRequest, client, selectedMealEntities);

        // Guardar el pedido en la base de datos
        orderRepository.save(orderEntityMapper.toOrderEntity(order));
    }

    private static Order getOrder(OrderRequest orderRequest, UserEntity client, List<MealEntity> selectedMealEntities) {
        User user = new User(client.getId(), client.getName(), client.getDocument(), client.getPhone(), client.getBirthdate(), client.getEmail(),  client.getRole(), client.getPassword());

        Order order = new Order(client.getId(), user, orderRequest.getRestaurant(), selectedMealEntities, OrderStatus.PENDING, orderRequest.getQuantity());
        order.setUser(user);
        order.setRestaurant(orderRequest.getRestaurant());
        order.setMeals(orderRequest.getMeals());
        order.setQuantity(orderRequest.getQuantity());
        order.setOrderStatus(OrderStatus.PENDING); // Estado inicial del pedido
        return order;
    }
}
