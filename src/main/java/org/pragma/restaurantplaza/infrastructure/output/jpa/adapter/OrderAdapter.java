package org.pragma.restaurantplaza.infrastructure.output.jpa.adapter;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import lombok.RequiredArgsConstructor;
import org.pragma.restaurantplaza.application.dto.OrderRequest;
import org.pragma.restaurantplaza.application.dto.OrderResponse;
import org.pragma.restaurantplaza.application.dto.UserResponse;
import org.pragma.restaurantplaza.domain.model.*;
import org.pragma.restaurantplaza.domain.spi.IOrderPersistencePort;
import org.pragma.restaurantplaza.infrastructure.exception.EntityNotFoundException;
import org.pragma.restaurantplaza.infrastructure.exception.InvalidStateException;
import org.pragma.restaurantplaza.infrastructure.output.jpa.entity.MealEntity;
import org.pragma.restaurantplaza.infrastructure.output.jpa.entity.OrderEntity;
import org.pragma.restaurantplaza.infrastructure.output.jpa.entity.RestaurantEntity;
import org.pragma.restaurantplaza.infrastructure.output.jpa.entity.UserEntity;
import org.pragma.restaurantplaza.infrastructure.output.jpa.mapper.MealEntityMapper;
import org.pragma.restaurantplaza.infrastructure.output.jpa.mapper.OrderEntityMapper;
import org.pragma.restaurantplaza.infrastructure.output.jpa.mapper.RestaurantEntityMapper;
import org.pragma.restaurantplaza.infrastructure.output.jpa.mapper.UserEntityMapper;
import org.pragma.restaurantplaza.infrastructure.output.jpa.repository.IMealRepository;
import org.pragma.restaurantplaza.infrastructure.output.jpa.repository.IOrderRepository;
import org.pragma.restaurantplaza.infrastructure.output.jpa.repository.IRestaurantRepository;
import org.pragma.restaurantplaza.infrastructure.output.jpa.repository.IUserRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderAdapter implements IOrderPersistencePort {

    private static final String ACCOUNT_SID = "AC62c10541448481c0bcc69e916652a4e4";
    private static final String AUTH_TOKEN = "b486013694652cdc5d1088bbd2db594b";
    private static final String TWILIO_PHONE_NUMBER = "+14788224490";
    private final IOrderRepository orderRepository;
    private final OrderEntityMapper orderEntityMapper;
    private final IUserRepository userRepository;
    private final MealEntityMapper mealEntityMapper;
    private final UserEntityMapper userEntityMapper;
    private final RestaurantEntityMapper restaurantEntityMapper;
    private final IRestaurantRepository restaurantRepository;
    private final IMealRepository mealRepository;


   /* @Override
   /* public void createOrder(OrderRequest orderRequest) {
        UserEntity user = userRepository.findById(orderRequest.getId())
                .orElseThrow(() -> new EntityNotFoundException("Client not found"));

        List<MealEntity> selectedMealEntities = selectedMeals.stream()
                .map(mealEntityMapper::toMealEntity)
                .toList();
        Order order = getOrder(orderRequest, user, selectedMealEntities);

        orderRepository.save(orderEntityMapper.toOrderEntity(order));


        sendOrderNotification(user, "1234", orderRequest);
        createStatusLog(orderRequest);

    }*/

    public OrderEntity createOrder(Long userId, Long restaurantId, List<OrderResponse> orderItems) {

        if (hasActiveOrders(userId)) {
            throw new IllegalStateException("El usuario tiene pedidos en proceso.");
        }

        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado."));

        RestaurantEntity restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new EntityNotFoundException("Restaurante no encontrado."));


        OrderEntity order = new OrderEntity();
        order.setUser(user);
        order.setRestaurant(restaurant);
        order.setOrderStatus(OrderStatus.PENDING);
        order.setQuantity(orderItems.size());
        order.setTimestamp(LocalDateTime.now());
        order.setCreateAt(LocalDateTime.now());
        order.setUpdateAt(LocalDateTime.now());


        int totalAmount = 0;
        for (OrderResponse orderResponse : orderItems) {
            MealEntity meal = mealRepository.findById(orderResponse.getId())
                    .orElseThrow(() -> new EntityNotFoundException("Plato no encontrado."));


            if (!meal.getRestaurantId().equals(restaurantId)) {
                throw new IllegalArgumentException("El plato no pertenece al restaurante especificado.");
            }


            if (orderResponse.getQuantity() <= 0) {
                throw new IllegalArgumentException("La cantidad del plato debe ser mayor que cero.");
            }

            totalAmount += meal.getPrice() * orderResponse.getQuantity();
            order.getMeals().add(meal);
        }

        order.setTotalAmount(totalAmount);
        orderRepository.save(order);

        return order;
    }

    private boolean hasActiveOrders(Long userId) {
        List<OrderStatus> activeStatuses = Arrays.asList(OrderStatus.PENDING, OrderStatus.IN_PREPARATION, OrderStatus.READY);

        return orderRepository.existsByUserIdAndOrderStatusIn(userId, activeStatuses);
    }
}

    /*private static Order getOrder(OrderRequest orderRequest, UserEntity client, List<MealEntity> selectedMealEntities) {
        User user = new User(client.getId(), client.getName(), client.getDocument(), client.getPhone(), client.getBirthdate(), client.getEmail(), client.getRole(), client.getPassword());

        Order order = getOrder(client.getId(), user, orderRequest.getRestaurant(), selectedMealEntities, OrderStatus.PENDING, orderRequest.getAssignedEmployeeId(), orderRequest.getQuantity(), orderRequest.getSecurityPin(), orderRequest.getCreateAt(), orderRequest.getUpdateAt(), orderRequest.getEstimatedTime());
        order.setUser(user);
       /* order.setRestaurant(orderRequest.getRestaurant());
        order.setMeals(orderRequest.getMeals());
        order.setQuantity(orderRequest.getQuantity());
        order.setOrderStatus(OrderStatus.PENDING);
        return order;
    }


    public void sendOrderNotification(UserEntity user, String pin, OrderRequest orderRequest) {
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
        String messageBody = "";
        if (orderRequest.getOrderStatus().equals(OrderStatus.PENDING))
            messageBody = "Your Order is Pending. ";

        if (orderRequest.getOrderStatus().equals(OrderStatus.IN_PREPARATION))
            messageBody = "Your order is in preparation.";

        if (orderRequest.getOrderStatus().equals(OrderStatus.READY))
            messageBody = "Your order is ready to be picked up. ";

        if (orderRequest.getOrderStatus().equals(OrderStatus.DELIVERED))
            messageBody = "Your order was delivered. ";

        if (orderRequest.getOrderStatus().equals(OrderStatus.CANCELED))
            messageBody = "Your order was cancelled. ";


        Message message = Message.creator(
                new PhoneNumber(user.getPhone()),
                new PhoneNumber(TWILIO_PHONE_NUMBER),
                messageBody + "Present the following security pin: " + pin
        ).create();

        System.out.println("Message SID: " + message.getSid());

    }

    @Override
    public void markOrderAsDelivered(String providedPin, Long orderId) {
        Order order = getOrderById(orderId);

        if (order.getOrderStatus() == OrderStatus.READY && providedPin.equals(order.getSecurityPin())) {
            order.setOrderStatus(OrderStatus.DELIVERED);
            UserEntity userEntity = userEntityMapper.toUserEntity(order.getUser());

            OrderRequest orderRequest = new OrderRequest(
                    order.getId(),
                    userEntityMapper.toUserResponse(userEntity),
                    order.getRestaurant(),
                    order.getMeals(),
                    OrderStatus.DELIVERED,
                    order.getAssignedEmployeeId(),
                    order.getQuantity(),
                    order.getSecurityPin(),
                    order.getCreateAt(),
                    LocalDateTime.now(),
                    ChronoUnit.MINUTES.between(order.getCreateAt(), LocalDateTime.now())
            );

            sendOrderNotification(userEntity, providedPin, orderRequest);
            createStatusLog(orderRequest);
        } else {
            throw new InvalidStateException("Cannot mark as delivered");
        }
    }

    public Order getOrderById(Long orderId) {
        OrderEntity orderEntity = orderRepository.findById(orderId).orElseThrow();
        return orderEntityMapper.toOrder(orderEntity);
    }

    @Override
    public void cancelOrder(OrderRequest orderRequest) {
        if (orderRequest.getOrderStatus() == OrderStatus.PENDING) {
            orderRequest.setOrderStatus(OrderStatus.CANCELED);
            createStatusLog(orderRequest);
        } else {
            throw new InvalidStateException("Can't cancel order");
        }
    }

    @Override
    public void createStatusLog(OrderRequest orderRequest) {
        OrderEntity log = new OrderEntity();
        log.setOrderStatus(orderRequest.getOrderStatus());
        log.setTimestamp(LocalDateTime.now());
        orderRepository.save(log);

    }

   /* @Override
    public List<OrderResponse> calculateOrderEfficiency() {
        List<OrderEntity> orders = orderRepository.findAll();
        List<OrderResponse> orderEfficiencyList = new ArrayList<>();

        for (OrderEntity orderEntity : orders) {
            LocalDateTime startTime = orderEntity.getCreateAt();
            LocalDateTime endTime = orderEntity.getUpdateAt();

            long estimatedTime = ChronoUnit.MINUTES.between(startTime, endTime);

            User user = userEntityMapper.toUser(orderEntity.getUser());
            Restaurant restaurant = restaurantEntityMapper.toRestaurant(orderEntity.getRestaurant());

            OrderResponse orderEfficiency = new OrderResponse(
                    orderEntity.getId(),
                    user,
                    restaurant,
                    orderEntity.getMeals(),
                    orderEntity.getOrderStatus(),
                    orderEntity.getAssignedEmployeeId(),
                    orderEntity.getQuantity(),
                    orderEntity.getSecurityPin(),
                    startTime,
                    endTime,
                    estimatedTime
            );
            orderEfficiencyList.add(orderEfficiency);
        }

        return orderEfficiencyList;


    }

    @Override
    public List<UserResponse> calculateEmployeeEfficiency() {
        List<UserEntity> employees = userRepository.findByRole("EMPLOYEE");
        List<UserResponse> employeeEfficiencyList = new ArrayList<>();

        for (UserEntity employee : employees) {
            List<OrderEntity> ordersHandledByEmployee = orderRepository.findByAssignedEmployeeIdAndOrderStatus(employee.getId(), OrderStatus.DELIVERED);

            long totalDuration = 0;
            int totalOrdersHandled = ordersHandledByEmployee.size();

            for (OrderEntity orderEntity : ordersHandledByEmployee) {
                LocalDateTime startTime = orderEntity.getCreateAt();
                LocalDateTime endTime = orderEntity.getUpdateAt();

                totalDuration += ChronoUnit.MINUTES.between(startTime, endTime);
            }

            if (totalOrdersHandled > 0) {
                long averageDuration = totalDuration / totalOrdersHandled;
                UserResponse employeeEfficiency = new UserResponse(employee.getId(), employee.getName(), employee.getDocument(), employee.getPhone(), employee.getBirthdate(), employee.getEmail(), employee.getRole(), employee.getPassword(), averageDuration);
                employeeEfficiencyList.add(employeeEfficiency);
            }
        }

        return employeeEfficiencyList;
    }

    @Override
    public void assignOrderToEmployeeAndChangeStatus(Long orderId, Long employeeId) {
        Order order = getOrderById(orderId);

        UserEntity employee = userRepository.findById(employeeId)
                .orElseThrow(() -> new EntityNotFoundException("Employee not found"));

        if (!order.getOrderStatus().equals(OrderStatus.PENDING)) {
            throw new InvalidStateException("Cannot assign an order that is not pending");
        }

        order.setAssignedEmployeeId(employee.getId());
        order.setOrderStatus(OrderStatus.IN_PREPARATION);

        orderRepository.save(orderEntityMapper.toOrderEntity(order));
    }


*/
