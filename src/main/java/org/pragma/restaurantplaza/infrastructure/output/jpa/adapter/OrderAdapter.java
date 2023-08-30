package org.pragma.restaurantplaza.infrastructure.output.jpa.adapter;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import lombok.RequiredArgsConstructor;
import org.pragma.restaurantplaza.application.dto.OrderRequest;
import org.pragma.restaurantplaza.domain.model.Meal;
import org.pragma.restaurantplaza.domain.model.Order;
import org.pragma.restaurantplaza.domain.model.OrderStatus;
import org.pragma.restaurantplaza.domain.model.User;
import org.pragma.restaurantplaza.domain.spi.IOrderPersistencePort;
import org.pragma.restaurantplaza.infrastructure.exception.EntityNotFoundException;
import org.pragma.restaurantplaza.infrastructure.exception.InvalidStateException;
import org.pragma.restaurantplaza.infrastructure.output.jpa.entity.MealEntity;
import org.pragma.restaurantplaza.infrastructure.output.jpa.entity.OrderEntity;
import org.pragma.restaurantplaza.infrastructure.output.jpa.entity.UserEntity;
import org.pragma.restaurantplaza.infrastructure.output.jpa.mapper.MealEntityMapper;
import org.pragma.restaurantplaza.infrastructure.output.jpa.mapper.OrderEntityMapper;
import org.pragma.restaurantplaza.infrastructure.output.jpa.mapper.UserEntityMapper;
import org.pragma.restaurantplaza.infrastructure.output.jpa.repository.IOrderRepository;
import org.pragma.restaurantplaza.infrastructure.output.jpa.repository.IUserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
@Service
@RequiredArgsConstructor
public class OrderAdapter implements IOrderPersistencePort {

    private static final String ACCOUNT_SID = "YOUR_TWILIO_ACCOUNT_SID";
    private static final String AUTH_TOKEN = "YOUR_TWILIO_AUTH_TOKEN";
    private static final String TWILIO_PHONE_NUMBER = "YOUR_TWILIO_PHONE_NUMBER";
    private final IOrderRepository orderRepository;
    private final OrderEntityMapper orderEntityMapper;
    private final IUserRepository userRepository;
    private final MealEntityMapper mealEntityMapper;
    private final UserEntityMapper userEntityMapper;
    private List<Meal> selectedMeals;

    @Override
    public void createOrder(OrderRequest orderRequest) {
        UserEntity user = userRepository.findById(orderRequest.getId())
                .orElseThrow(() -> new EntityNotFoundException("Client not found"));

        List<MealEntity> selectedMealEntities = selectedMeals.stream()
                .map(mealEntityMapper::toMealEntity)
                .collect(Collectors.toList());
        Order order = getOrder(orderRequest, user, selectedMealEntities);

        orderRepository.save(orderEntityMapper.toOrderEntity(order));

        sendOrderNotification(user, "1234", orderRequest);

    }

    private static Order getOrder(OrderRequest orderRequest, UserEntity client, List<MealEntity> selectedMealEntities) {
        User user = new User(client.getId(), client.getName(), client.getDocument(), client.getPhone(), client.getBirthdate(), client.getEmail(),  client.getRole(), client.getPassword());

        Order order = new Order(client.getId(), user, orderRequest.getRestaurant(), selectedMealEntities, OrderStatus.PENDING, orderRequest.getAssignedEmployeeId(), orderRequest.getQuantity(),orderRequest.getSecurityPin());
        order.setUser(user);
        order.setRestaurant(orderRequest.getRestaurant());
        order.setMeals(orderRequest.getMeals());
        order.setQuantity(orderRequest.getQuantity());
        order.setOrderStatus(OrderStatus.PENDING);
        return order;
    }



    public void sendOrderNotification(UserEntity user, String pin,OrderRequest orderRequest){
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
        String messageBody = "";
        if(orderRequest.getOrderStatus().equals(OrderStatus.PENDING))
            messageBody = "Tu pedido está en pendientes. ";

        if(orderRequest.getOrderStatus().equals(OrderStatus.IN_PREPARATION))
            messageBody = "Tu pedido está en preparacion para recoger. ";

        if(orderRequest.getOrderStatus().equals(OrderStatus.READY))
            messageBody = "Tu pedido está listo para recoger. ";

        if(orderRequest.getOrderStatus().equals(OrderStatus.DELIVERED))
            messageBody = "Tu pedido fue entregado. ";

        if(orderRequest.getOrderStatus().equals(OrderStatus.CANCELED))
            messageBody = "Tu pedido fue cancelado. ";


        Message message = Message.creator(
                new PhoneNumber(user.getPhone()),
                new PhoneNumber(TWILIO_PHONE_NUMBER),
                messageBody+"Presenta el siguiente pin de seguridad: "+pin
        ).create();

       System.out.println("Mensaje SID: " + message.getSid());

    }

    public void markAsDelivered(String providedPin,OrderRequest orderRequest){
        if (orderRequest.getOrderStatus() == OrderStatus.READY && providedPin.equals(orderRequest.getSecurityPin())) {
            orderRequest.setOrderStatus(OrderStatus.DELIVERED);
            UserEntity user = userEntityMapper.toUserEntity(orderRequest.getUser());
            sendOrderNotification( user,providedPin, orderRequest);
        } else {
            throw new InvalidStateException("No se puede marcar como entregado");
        }
    }

    public Order getOrderById(Long orderId) {
        OrderEntity orderEntity = orderRepository.findById(orderId).orElseThrow();
        return orderEntityMapper.toOrder(orderEntity);
    }

    public void cancelOrder(OrderRequest orderRequest) {
        if (orderRequest.getOrderStatus() == OrderStatus.PENDING) {
            orderRequest.setOrderStatus(OrderStatus.CANCELED);
        } else {
            throw new InvalidStateException("No se puede cancelar el pedido");
        }
    }
}
