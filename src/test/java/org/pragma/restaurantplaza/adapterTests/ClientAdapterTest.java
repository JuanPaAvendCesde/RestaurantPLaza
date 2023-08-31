package org.pragma.restaurantplaza.adapterTests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.pragma.restaurantplaza.application.dto.MealResponse;
import org.pragma.restaurantplaza.application.dto.OrderRequest;
import org.pragma.restaurantplaza.application.dto.OrderResponse;
import org.pragma.restaurantplaza.domain.model.*;
import org.pragma.restaurantplaza.infrastructure.exception.*;
import org.pragma.restaurantplaza.infrastructure.output.jpa.adapter.OrderAdapter;
import org.pragma.restaurantplaza.infrastructure.output.jpa.adapter.RestaurantAdapter;
import org.pragma.restaurantplaza.infrastructure.output.jpa.adapter.UserAdapter;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ClientAdapterTest {

    @Mock
    private IUserRepository userRepository;

    @Mock
    private UserEntityMapper userEntityMapper;
    @InjectMocks
    private UserAdapter userAdapter;
    @Mock
    private IOrderRepository orderRepository;


    @Mock
    private IMealRepository mealRepository;

    @Mock
    private OrderEntityMapper orderEntityMapper;


    @Mock
    private IRestaurantRepository restaurantRepository;


    @Mock
    private RestaurantEntityMapper restaurantEntityMapper;

    @Mock
    private MealEntityMapper mealEntityMapper;


    @InjectMocks
    private RestaurantAdapter restaurantAdapter;


    @InjectMocks
    private OrderAdapter orderAdapter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userAdapter = new UserAdapter(userRepository, userEntityMapper);
        orderAdapter = new OrderAdapter(orderRepository, orderEntityMapper, userRepository, mealEntityMapper, userEntityMapper, restaurantEntityMapper);
    }
//Historia7---------------------------------------------------------------------------
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

   //Historia8---------------------------------------------------------------------------
    @Test
    void testFindAllRestaurants() {
        int page = 0;
        int size = 10;

        List<RestaurantEntity> restaurantEntities = new ArrayList<>();


        Pageable pageable = PageRequest.of(page, size);
        Page<RestaurantEntity> restaurantEntityPage = new PageImpl<>(restaurantEntities, pageable, restaurantEntities.size());

        when(restaurantRepository.findAll(pageable)).thenReturn(restaurantEntityPage);

        List<Restaurant> restaurantList = restaurantEntities.stream()
                .map(restaurantEntityMapper::toRestaurant)
                .collect(Collectors.toList());
        Page<Restaurant> expectedPage = new PageImpl<>(restaurantList, pageable, restaurantEntities.size());


        Page<Restaurant> result = restaurantAdapter.findAll(pageable);


        assertEquals(expectedPage.getTotalElements(), result.getTotalElements());

    }

//Historia9---------------------------------------------------------------------------

    @Test
    void testGetMenuByCategory() {
        Long restaurantId = 1L;
        String category = "Main Course";
        String name = "RestaurantName";
        int page = 0;
        int size = 10;

        RestaurantEntity restaurant = new RestaurantEntity();
        restaurant.setId(restaurantId);

        List<MealEntity> mealEntities = new ArrayList<>();


        Pageable pageable = PageRequest.of(page, size);
        Page<MealEntity> mealPage = new PageImpl<>(mealEntities, pageable, mealEntities.size());

        when(restaurantRepository.findById(restaurantId)).thenReturn(java.util.Optional.of(restaurant));
        when(mealRepository.findByRestaurantIdAndCategory(restaurant, category, pageable)).thenReturn(mealPage);


        Page<MealResponse> result = restaurantAdapter.getRestaurantMenuByCategory(restaurantId, name, category, page, size);

        assertNotNull(result);
        assertEquals(0, result.getNumber());
        assertEquals(10, result.getSize());

    }

    //Historia11---------------------------------------------------------------------------

    @Test
    void testCreateOrder_Success() {

        UserEntity clientEntity = new UserEntity();
        when(userRepository.findById(any())).thenReturn(java.util.Optional.of(clientEntity));


        List<MealEntity> selectedMealEntities = new ArrayList<>();
        when(mealRepository.findAllById(anyCollection())).thenReturn(selectedMealEntities);

        List<Long> selectedMealIds = Arrays.asList(1L, 2L, 3L);


        List<MealEntity> selectedMeals = mealRepository.findAllById(selectedMealIds);


        OrderRequest orderRequest = new OrderRequest( 1L, new User(1L, "user", 3265326, "+573226094632", LocalDate.of(2015, 8, 20), "aa@aa.com", "12334", "CLIENT" ),new Restaurant(1L, "name", 213123, "rewarded", "+573005698325", "",new User(1L, "user", 3265326, "+573226094632", LocalDate.of(2015, 8, 20), "aa@aa.com", "12334", "Owner" ), selectedMealEntities ), selectedMealEntities, OrderStatus.PENDING, 2L,1, "1234", LocalDateTime.now(),null,50);
        orderRequest.setId(1L);
        orderRequest.setUser(new User(1L, "user", 3265326, "+573226094632", LocalDate.of(1995, 8, 20), "aa@aa.com", "12334", "CLIENT"));
        orderRequest.setRestaurant(new Restaurant(1L, "Restaurant", 12345, "Address", "123456789", "urlLogo", new User(2L, "user", 3265326, "+573226094632", LocalDate.of(1995, 8, 20), "aa@aa.com", "12334", "CLIENT"), null));





        OrderEntity orderEntity = new OrderEntity();
        when(orderEntityMapper.toOrderEntity(any())).thenReturn(orderEntity);


        orderAdapter.createOrder(orderRequest);


        verify(orderRepository).save(orderEntity);
    }
   // historia16---------------------------------------------------------------------------
    @Test
    void cancelOrder_ValidStatus() {
        List<MealEntity> selectedMealEntities = new ArrayList<>();
        List<Long> selectedMealIds = Arrays.asList(1L, 2L, 3L);

        List<MealEntity> selectedMeals = mealRepository.findAllById(selectedMealIds);
        OrderRequest orderRequest = new OrderRequest( 1L, new User(1L, "user", 3265326, "+573226094632", LocalDate.of(2015, 8, 20), "aa@aa.com", "12334", "CLIENT" ),new Restaurant(1L, "name", 213123, "rewarded", "+573005698325", "",new User(1L, "user", 3265326, "+573226094632", LocalDate.of(2015, 8, 20), "aa@aa.com", "12334", "Owner" ), selectedMealEntities ), selectedMealEntities, OrderStatus.PENDING, 2L,1, "1234", LocalDateTime.now(),null,50);
        orderRequest.setOrderStatus(OrderStatus.PENDING);

        // Act
        orderAdapter.cancelOrder(orderRequest);

        // Assert
        assertEquals(OrderStatus.CANCELED, orderRequest.getOrderStatus());
        verify(orderAdapter, times(1)).createStatusLog(orderRequest);
    }

    @Test
    void cancelOrder_InvalidStatus() {
        List<MealEntity> selectedMealEntities = new ArrayList<>();
        List<Long> selectedMealIds = Arrays.asList(1L, 2L, 3L);

        List<MealEntity> selectedMeals = mealRepository.findAllById(selectedMealIds);
        OrderRequest orderRequest = new OrderRequest( 1L, new User(1L, "user", 3265326, "+573226094632", LocalDate.of(2015, 8, 20), "aa@aa.com", "12334", "CLIENT" ),new Restaurant(1L, "name", 213123, "rewarded", "+573005698325", "",new User(1L, "user", 3265326, "+573226094632", LocalDate.of(2015, 8, 20), "aa@aa.com", "12334", "Owner" ), selectedMealEntities ), selectedMealEntities, OrderStatus.PENDING, 2L,1, "1234", LocalDateTime.now(),null,50);

        orderRequest.setOrderStatus(OrderStatus.READY);

        // Act & Assert
        assertThrows(InvalidStateException.class, () -> orderAdapter.cancelOrder(orderRequest));
        verifyNoMoreInteractions(orderAdapter);
    }

    //Historia17---------------------------------------------------------------------------
    @Test
    void calculateOrderEfficiency() {
        // Arrange
        LocalDateTime startTime = LocalDateTime.of(2023, 8, 13, 12, 0);
        LocalDateTime endTime = LocalDateTime.of(2023, 8, 13, 12, 30);
        long estimatedTime = ChronoUnit.MINUTES.between(startTime, endTime);

        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setId(1L);
        orderEntity.setCreateAt(startTime);
        orderEntity.setUpdateAt(endTime);
        orderEntity.setUser(new UserEntity());
        orderEntity.setRestaurant(new RestaurantEntity());
        orderEntity.setMeals(new ArrayList<>());
        orderEntity.setOrderStatus(OrderStatus.READY);
        orderEntity.setAssignedEmployeeId(1L);
        orderEntity.setQuantity(2);
        orderEntity.setSecurityPin("1234");

        List<OrderEntity> orders = new ArrayList<>();
        orders.add(orderEntity);

        when(orderRepository.findAll()).thenReturn(orders);

        // Act
        List<OrderResponse> orderEfficiencyList = orderAdapter.calculateOrderEfficiency();

        // Assert
        assertEquals(1, orderEfficiencyList.size());
        OrderResponse orderEfficiency = orderEfficiencyList.get(0);
        assertEquals(1L, orderEfficiency.getId());
        assertEquals(estimatedTime, orderEfficiency.getEstimatedTime());
        verify(orderRepository, times(1)).findAll();
        verify(userEntityMapper, times(1)).toUser(any());
        verify(restaurantEntityMapper, times(1)).toRestaurant(any());
    }
}