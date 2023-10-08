package org.pragma.restaurantplaza.adapterTests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.pragma.restaurantplaza.application.dto.OrderRequest;
import org.pragma.restaurantplaza.application.dto.OrderResponse;
import org.pragma.restaurantplaza.application.handler.OrderHandler;
import org.pragma.restaurantplaza.domain.model.Order;
import org.pragma.restaurantplaza.domain.model.OrderStatus;
import org.pragma.restaurantplaza.domain.model.Restaurant;
import org.pragma.restaurantplaza.domain.model.User;
import org.pragma.restaurantplaza.infrastructure.exception.InvalidStateException;
import org.pragma.restaurantplaza.infrastructure.output.jpa.adapter.OrderAdapter;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EmployeeAdapterTest {

    @Mock
    private IUserRepository userRepository;

    @Mock
    private UserEntityMapper userEntityMapper;

    private OrderAdapter orderAdapter;

    @Mock
    private IRestaurantRepository restaurantRepository;

    @Mock
    private IOrderRepository orderRepository;

    @InjectMocks
    private OrderHandler orderHandler;

    @Mock
    private OrderEntityMapper orderEntityMapper;

    @Mock
    private MealEntityMapper mealEntityMapper;

    @Mock
    private RestaurantEntityMapper restaurantEntityMapper;
    @Mock
    private IMealRepository mealRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        orderAdapter = new OrderAdapter(orderRepository, orderEntityMapper, userRepository, mealEntityMapper, userEntityMapper, restaurantEntityMapper,restaurantRepository,mealRepository);
    }

//historia12----------------------------------------------------------------------------------------------------------------
  /*  @Test
    void testGetAssignedOrdersByStateAndRestaurant() {
        Long employeeId = 1L;
        Long restaurantId = 2L;
        OrderStatus state = OrderStatus.IN_PREPARATION;
        Pageable pageable = Pageable.ofSize(10).withPage(0);

        UserEntity user = new UserEntity();
        RestaurantEntity restaurant = new RestaurantEntity();
        List<OrderEntity> orderEntities = new ArrayList<>();


        Page<OrderEntity> orderEntityPage = new PageImpl<>(orderEntities, pageable, orderEntities.size());

        when(userRepository.findById(employeeId)).thenReturn(Optional.of(user));
        when(restaurantRepository.findById(restaurantId)).thenReturn(Optional.of(restaurant));
        when(orderRepository.findByOrderStatusAndAssignedEmployeeIdAndRestaurant(state, employeeId, restaurant, pageable));


        Page<OrderResponse> result = orderHandler.getAssignedOrdersByStateAndRestaurant(state, employeeId, restaurantId, pageable);


        assertEquals(orderEntityPage.getTotalElements(), result.getTotalElements());

    }
    //historia13----------------------------------------------------------------------------------------------------------------
    @Test
     void testAssignOrderToEmployeeAndChangeStatus_Success() {
        Long orderId = 1L;
        Long employeeId = 2L;

        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setId(orderId);
        orderEntity.setOrderStatus(OrderStatus.PENDING);

        UserEntity employeeEntity = new UserEntity();
        employeeEntity.setId(employeeId);

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(orderEntity));
        when(userRepository.findById(employeeId)).thenReturn(Optional.of(employeeEntity));

        orderAdapter.assignOrderToEmployeeAndChangeStatus(orderId, employeeId);

        verify(orderRepository, times(1)).findById(orderId);
        verify(userRepository, times(1)).findById(employeeId);

        // Verify that the order's assigned employee and status were updated
        assertEquals(employeeId, orderEntity.getAssignedEmployeeId());
        assertEquals(OrderStatus.IN_PREPARATION, orderEntity.getOrderStatus());

        verify(orderRepository, times(1)).save(orderEntity);
    }

    @Test
    void testAssignOrderToEmployeeAndChangeStatus_EmployeeNotFound() {
        Long orderId = 1L;
        Long employeeId = 2L;

        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setId(orderId);
        orderEntity.setOrderStatus(OrderStatus.PENDING);

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(orderEntity));
        when(userRepository.findById(employeeId)).thenReturn(Optional.empty());

        orderAdapter.assignOrderToEmployeeAndChangeStatus(orderId, employeeId);

        // This test should throw an EntityNotFoundException
    }

    @Test
    void testAssignOrderToEmployeeAndChangeStatus_OrderNotPending() {
        Long orderId = 1L;
        Long employeeId = 2L;

        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setId(orderId);
        orderEntity.setOrderStatus(OrderStatus.IN_PREPARATION);

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(orderEntity));

        orderAdapter.assignOrderToEmployeeAndChangeStatus(orderId, employeeId);

        // This test should throw an InvalidStateException
    }
    //historia15----------------------------------------------------------------------------------------------------------------
    @Test
    void markOrderAsDelivered_ValidPinAndStatus() {
        // Arrange
        Long orderId = 1L;
        String providedPin = "1234";
        Order order = new Order( 1L, new User(1L, "name", 123456, "phone", LocalDate.now(), "email", "password", "role"), new Restaurant(1L,"aa",1234,"address","+573226094632","url",null,null  ), new ArrayList<>(), OrderStatus.READY, 1L, 1, providedPin, null, null, null);


        when(orderAdapter.getOrderById(orderId)).thenReturn(order);
        when(userEntityMapper.toUserEntity(order.getUser())).thenReturn(new UserEntity());

        // Act
        orderAdapter.markOrderAsDelivered(providedPin, orderId);

        // Assert
        assertEquals(OrderStatus.DELIVERED, order.getOrderStatus());
        verify(orderAdapter, times(1)).getOrderById(orderId);
        verify(orderAdapter, times(1)).sendOrderNotification(any(UserEntity.class), eq(providedPin), any(OrderRequest.class));
    }

    @Test
    void markOrderAsDelivered_InvalidStatus() {
        // Arrange
        Long orderId = 1L;
        String providedPin = "1234";
        Order order = new Order( 1L, new User(1L, "name", 123456, "phone", LocalDate.now(), "email", "password", "role"), new Restaurant(1L,"aa",1234,"address","+573226094632","url",null,null  ), new ArrayList<>(), OrderStatus.READY, 1L, 1, providedPin, null, null, null);
        order.setId(orderId);
        order.setOrderStatus(OrderStatus.PENDING);
        order.setSecurityPin(providedPin);



        when(orderAdapter.getOrderById(orderId)).thenReturn(order);

        // Act & Assert
        assertThrows(InvalidStateException.class, () -> orderAdapter.markOrderAsDelivered(providedPin, orderId));
        verify(orderAdapter, times(1)).getOrderById(orderId);
        verifyNoMoreInteractions(orderAdapter);
    }*/
}

