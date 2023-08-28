package org.pragma.restaurantplaza.infrastructure.output.jpa.adapter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.pragma.restaurantplaza.application.dto.OrderRequest;
import org.pragma.restaurantplaza.domain.model.*;
import org.pragma.restaurantplaza.infrastructure.exception.*;
import org.pragma.restaurantplaza.infrastructure.output.jpa.entity.MealEntity;
import org.pragma.restaurantplaza.infrastructure.output.jpa.entity.OrderEntity;
import org.pragma.restaurantplaza.infrastructure.output.jpa.entity.UserEntity;
import org.pragma.restaurantplaza.infrastructure.output.jpa.mapper.OrderEntityMapper;
import org.pragma.restaurantplaza.infrastructure.output.jpa.mapper.UserEntityMapper;
import org.pragma.restaurantplaza.infrastructure.output.jpa.repository.IMealRepository;
import org.pragma.restaurantplaza.infrastructure.output.jpa.repository.IOrderRepository;
import org.pragma.restaurantplaza.infrastructure.output.jpa.repository.IUserRepository;
import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ClientAdapterTest {

    @Mock
    private IUserRepository userRepository;

    @Mock
    private UserEntityMapper userEntityMapper;

    private UserAdapter userAdapter;
    @Mock
    private IOrderRepository orderRepository;


    @Mock
    private IMealRepository mealRepository;

    @Mock
    private OrderEntityMapper orderEntityMapper;


    @InjectMocks
    private OrderAdapter orderAdapter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userAdapter = new UserAdapter(userRepository, userEntityMapper);
    }



    @Test
     void testCreateOrder_Success() {
        // Mocking user data
        UserEntity clientEntity = new UserEntity();
        when(userRepository.findById(any())).thenReturn(java.util.Optional.of(clientEntity));

        // Mocking meal data
        List<MealEntity> selectedMealEntities = new ArrayList<>();
        when(mealRepository.findAllById(anyCollection())).thenReturn(selectedMealEntities);

        // Mocking order data
        OrderRequest orderRequest = new OrderRequest();
        orderRequest.setId(1L);
        orderRequest.setUser(new User(1L, "user", 3265326, "+573226094632", LocalDate.of(1995, 8, 20), "aa@aa.com", "12334", "CLIENT"));
        orderRequest.setRestaurant(new Restaurant(1L, "Restaurant", 12345, "Address", "123456789", "urlLogo", new User(2L, "user", 3265326, "+573226094632", LocalDate.of(1995, 8, 20), "aa@aa.com", "12334", "CLIENT"), null));

        List<Long> selectedMealIds = Arrays.asList(1L, 2L, 3L);

        // Fetch the actual MealEntity objects using the mealRepository and the list of IDs
        List<MealEntity> selectedMeals = mealRepository.findAllById(selectedMealIds);

        orderRequest.setMeals(selectedMeals);

        orderRequest.setQuantity(2);

        // Mocking order entity conversion
        OrderStatus orderStatus = OrderStatus.PENDING;
        OrderEntity orderEntity = new OrderEntity();
        when(orderEntityMapper.toOrderEntity(any())).thenReturn(orderEntity);

        // Perform the test
        orderAdapter.createOrder(orderRequest);

        // Verify the interactions
        verify(orderRepository).save(orderEntity);
    }









    @Test
    void saveClient_SuccessfulCreation() {

        User user = new User(1L, "user", 3265326, "+573226094632", LocalDate.of(2015, 8, 20), "aa@aa.com", "12334", "CLIENT");

        when(userRepository.findById(1L)).thenReturn(Optional.empty());
        when(userEntityMapper.toUserEntity(user)).thenReturn(new UserEntity());

        assertDoesNotThrow(() -> userAdapter.saveUser(user));
        verify(userRepository, times(1)).findById(1L);
        verify(userEntityMapper, times(1)).toUserEntity(user);
        verify(userRepository, times(1)).save(any(UserEntity.class));
    }

    @Test
    void saveClient_AlreadyExists() {

        User owner = new User(1L, "user", 3265326, "+573226094632", LocalDate.of(1995, 7, 8), "aa@aa.com", "12334", "CLIENT");

        when(userRepository.findById(1L)).thenReturn(Optional.of(new UserEntity()));

        assertThrows(UserAlreadyExistException.class, () -> userAdapter.saveUser(owner));
        verify(userRepository, times(1)).findById(1L);

    }
}