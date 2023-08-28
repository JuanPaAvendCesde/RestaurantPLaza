package org.pragma.restaurantplaza.infrastructure.output.jpa.adapter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.pragma.restaurantplaza.application.dto.MealResponse;
import org.pragma.restaurantplaza.application.dto.OrderRequest;
import org.pragma.restaurantplaza.domain.model.*;
import org.pragma.restaurantplaza.infrastructure.exception.*;
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
import java.util.*;
import java.util.stream.Collectors;

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


    @Mock
    private IRestaurantRepository restaurantRepository;


    @Mock
    private RestaurantEntityMapper restaurantEntityMapper;


    @InjectMocks
    private RestaurantAdapter restaurantService;


    @InjectMocks
    private OrderAdapter orderAdapter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userAdapter = new UserAdapter(userRepository, userEntityMapper);
    }



    @Test
     void testCreateOrder_Success() {

        UserEntity clientEntity = new UserEntity();
        when(userRepository.findById(any())).thenReturn(java.util.Optional.of(clientEntity));


        List<MealEntity> selectedMealEntities = new ArrayList<>();
        when(mealRepository.findAllById(anyCollection())).thenReturn(selectedMealEntities);


        OrderRequest orderRequest = new OrderRequest();
        orderRequest.setId(1L);
        orderRequest.setUser(new User(1L, "user", 3265326, "+573226094632", LocalDate.of(1995, 8, 20), "aa@aa.com", "12334", "CLIENT"));
        orderRequest.setRestaurant(new Restaurant(1L, "Restaurant", 12345, "Address", "123456789", "urlLogo", new User(2L, "user", 3265326, "+573226094632", LocalDate.of(1995, 8, 20), "aa@aa.com", "12334", "CLIENT"), null));

        List<Long> selectedMealIds = Arrays.asList(1L, 2L, 3L);


        List<MealEntity> selectedMeals = mealRepository.findAllById(selectedMealIds);

        orderRequest.setMeals(selectedMeals);

        orderRequest.setQuantity(2);


        OrderStatus orderStatus = OrderStatus.PENDING;
        OrderEntity orderEntity = new OrderEntity();
        when(orderEntityMapper.toOrderEntity(any())).thenReturn(orderEntity);


        orderAdapter.createOrder(orderRequest);


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
    @Test
    void testGetRestaurantMenuByCategory() {
        Long restaurantId = 1L;
        String category = "Main Course";
        int page = 0;
        int size = 10;

        RestaurantEntity restaurant = new RestaurantEntity();
        restaurant.setId(restaurantId);

        List<MealEntity> mealEntities = new ArrayList<>();


        Pageable pageable = PageRequest.of(page, size);
        Page<MealEntity> mealPage = new PageImpl<>(mealEntities, pageable, mealEntities.size());

        when(restaurantRepository.findById(restaurantId)).thenReturn(java.util.Optional.of(restaurant));
        when(mealRepository.findByRestaurantIdAndCategory(restaurant, category, pageable)).thenReturn(mealPage);


        Page<MealResponse> result = restaurantService.getRestaurantMenuByCategory(restaurantId, null, category, page, size);


    }

    @Test
    void testFindAll() {
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

        // Call the method being tested
        Page<Restaurant> result = restaurantService.findAll(pageable);

        // Assertions
        assertEquals(expectedPage.getTotalElements(), result.getTotalElements());
        // Perform additional assertions based on your expected data
    }
}