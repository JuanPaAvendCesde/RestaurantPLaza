package org.pragma.restaurantplaza.adapterTests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.pragma.restaurantplaza.application.dto.MealResponse;
import org.pragma.restaurantplaza.application.dto.OrderResponse;
import org.pragma.restaurantplaza.application.handler.OrderHandler;
import org.pragma.restaurantplaza.domain.model.*;
import org.pragma.restaurantplaza.infrastructure.exception.*;
import org.pragma.restaurantplaza.infrastructure.output.jpa.adapter.MealAdapter;
import org.pragma.restaurantplaza.infrastructure.output.jpa.adapter.RestaurantAdapter;
import org.pragma.restaurantplaza.infrastructure.output.jpa.adapter.UserAdapter;
import org.pragma.restaurantplaza.infrastructure.output.jpa.entity.MealEntity;
import org.pragma.restaurantplaza.infrastructure.output.jpa.entity.RestaurantEntity;
import org.pragma.restaurantplaza.infrastructure.output.jpa.entity.UserEntity;
import org.pragma.restaurantplaza.infrastructure.output.jpa.mapper.MealEntityMapper;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AdminAdapterTest {

    @Mock
    private IRestaurantRepository restaurantRepository;

    @Mock
    private RestaurantEntityMapper restaurantEntityMapper;

    @Mock
    private IMealRepository mealRepository;

    @Mock
    private IOrderRepository orderRepository;

    @Mock
    private IUserRepository userRepository;

    @Mock
    private OrderHandler orderHandler;

    private RestaurantAdapter restaurantAdapter;

    @Mock
    private UserEntityMapper userEntityMapper;

    private UserAdapter userAdapter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        restaurantAdapter = new RestaurantAdapter(restaurantRepository, restaurantEntityMapper, mealRepository,orderRepository);
        userAdapter = new UserAdapter(userRepository, userEntityMapper);
    }
//historia1-------------------------------------------------------------------------------------------
    @Test
    void saveOwner_SuccessfulCreation() {

        User user = new User(1L, "user", 3265326, "+573226094632", LocalDate.of(1995, 8, 20), "aa@aa.com", "12334", "OWNER");

        when(userRepository.findById(1L)).thenReturn(Optional.empty());
        when(userEntityMapper.toUserEntity(user)).thenReturn(new UserEntity());

        assertDoesNotThrow(() -> userAdapter.saveUser(user));
        verify(userRepository, times(1)).findById(1L);
        verify(userEntityMapper, times(1)).toUserEntity(user);
        verify(userRepository, times(1)).save(any(UserEntity.class));
    }


    @Test
    void saveOwner_AlreadyExists() {

        User owner = new User(1L, "user", 3265326, "+573226094632", LocalDate.of(1995, 7, 8), "aa@aa.com", "12334", "OWNER");

        when(userRepository.findById(1L)).thenReturn(Optional.of(new UserEntity()));

        assertThrows(UserAlreadyExistException.class, () -> userAdapter.saveUser(owner));
        verify(userRepository, times(1)).findById(1L);

    }

    @Test
    void saveOwner_UnderageOwner() {

        User owner = new User(1L, "user", 3265326, "+573226094632", LocalDate.of(2015, 7, 8), "aa@aa.com", "12334", "OWNER");

        owner.setId(2L);
        owner.setBirthdate(LocalDate.now().minusYears(17));

        when(userRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(UserMustBeAdultException.class, () -> userAdapter.saveUser(owner));
        verify(userRepository, times(1)).findById(2L);

    }

    @Test
    void saveOwner_InvalidEmailFormat() {

        User user = new User(1L, "user", 3265326, "+573226094632", LocalDate.of(1995, 8, 20), "email_nova-lido", "12334", "OWNER");

        InvalidEmailException exception = assertThrows(InvalidEmailException.class, () -> userAdapter.saveUser(user));
        assertEquals("Invalid email format", exception.getMessage());

    }

    @Test
    void saveOwner_InvalidPhoneNumberFormat() {

        User user = new User(1L, "user", 3265326, "45681", LocalDate.of(1995, 8, 20), "email@email.com", "12334", "OWNER");

        InvalidPhoneNumberException exception = assertThrows(InvalidPhoneNumberException.class, () -> userAdapter.saveUser(user));
        assertEquals("Invalid phone format", exception.getMessage());

    }

    @Test
    void saveOwner_InvalidDocumentFormat() {

        User user = new User(1L, "user", -3265326, "+573226094632", LocalDate.of(1995, 8, 20), "email@example.com", "12334", "Owner");


        InvalidDocumentException exception = assertThrows(InvalidDocumentException.class, () -> userAdapter.saveUser(user));
        assertEquals("Invalid document format", exception.getMessage());
    }
    //historia2-------------------------------------------------------------------------------------------
    @Test
    void saveRestaurant_SuccessfulCreation() {

        LocalDate birthdate = LocalDate.of(1995, 8, 20);
        User user = new User(1L, "user", 3265326, "+573226094632", birthdate, "aa@aa.com", "12334", "OWNER");
        Restaurant restaurant = new Restaurant(1L, "name", 213123, "newsdealer", "+573005698325", "", user, null);

        when(restaurantRepository.findById(1L)).thenReturn(Optional.empty());
        when(restaurantEntityMapper.toRestaurantEntity(restaurant)).thenReturn(new RestaurantEntity());
        when(restaurantRepository.findById(1L)).thenReturn(Optional.empty());
        when(restaurantEntityMapper.toRestaurantEntity(restaurant)).thenReturn(new RestaurantEntity());

        assertDoesNotThrow(() -> restaurantAdapter.saveRestaurant(restaurant, user));
        verify(restaurantRepository, times(1)).findById(1L);
        verify(restaurantEntityMapper, times(1)).toRestaurantEntity(restaurant);
        verify(restaurantRepository, times(1)).save(any(RestaurantEntity.class));
    }

    @Test
    void saveRestaurant_RestaurantAlreadyExists() {

        User user = new User(1L, "user", 3265326, "2132152", LocalDate.of(1995, 8, 20), "aa@aa.com", "12334", "OWNER");
        Restaurant restaurant = new Restaurant(1L, "name", 213123, "rewritable", "+573005698325", "", user, null);

        when(restaurantRepository.findById(1L)).thenReturn(Optional.of(new RestaurantEntity()));

        RestaurantAlreadyExistException exception = assertThrows(
                RestaurantAlreadyExistException.class,
                () -> restaurantAdapter.saveRestaurant(restaurant, user)
        );
        assertEquals("Restaurant already exists", exception.getMessage());
        verify(restaurantRepository, times(1)).findById(1L);
    }

    @Test
    void saveRestaurant_InvalidUserRole() {

        User user = new User(1L, "user", 3265326, "+573226094632", LocalDate.of(1995, 8, 20), "aa@aa.com", "12334", "CLIENT");
        Restaurant restaurant = new Restaurant(1L, "name", 213123, "rewarded", "+573005698325", "", user, null);

        when(restaurantRepository.findById(1L)).thenReturn(Optional.empty());

        InvalidUserRoleException exception = assertThrows(
                InvalidUserRoleException.class,
                () -> restaurantAdapter.saveRestaurant(restaurant, user)
        );
        assertEquals("User must have the 'Owner' role to create a restaurant", exception.getMessage());
        verify(restaurantRepository, times(1)).findById(1L);

    }

    @Test
    void saveRestaurant_InvalidNitFormat() {

        User user = new User(1L, "user", 3265326, "+573226094632", LocalDate.of(1995, 8, 20), "aa@aa.com", "12334", "OWNER");
        Restaurant restaurant = new Restaurant(1L, "name", -213123, "rewrite", "+573005698325", "", user, null);

        InvalidNitFormatException exception = assertThrows(
                InvalidNitFormatException.class,
                () -> restaurantAdapter.saveRestaurant(restaurant, user)
        );
        assertEquals("Invalid NIT format", exception.getMessage());
        verify(restaurantRepository, times(1)).findById(1L);
    }

    @Test
    void saveRestaurant_InvalidPhoneNumberFormat() {

        LocalDate birthdate = LocalDate.of(1995, 8, 20);
        User user = new User(1L, "user", 3265326, "+573226094632", birthdate, "aa@aa.com", "12334", "OWNER");
        Restaurant restaurant = new Restaurant(1L, "name", 213123, "rewrite", "8325", "", user, null);

        InvalidPhoneNumberException exception = assertThrows(InvalidPhoneNumberException.class, () -> restaurantAdapter.saveRestaurant(restaurant, user));
        assertEquals("Invalid restaurant phone format", exception.getMessage());
        verify(restaurantRepository, times(1)).findById(1L);
    }

    @Test
    void saveRestaurant_InvalidRestaurantName() {

        User user = new User(1L, "aaa", 3265326, "+573226094632", LocalDate.of(1995, 8, 20), "aa@aa.com", "12334", "OWNER");
        Restaurant restaurant = new Restaurant(1L, "12332", 213123, "rewrite", "+573005698325", "", user, null);

        InvalidRestaurantNameException exception = assertThrows(InvalidRestaurantNameException.class, () -> restaurantAdapter.saveRestaurant(restaurant, user));
        assertEquals("Restaurant name cannot consist of only numbers", exception.getMessage());
        verify(restaurantRepository, times(1)).findById(1L);
    }

    //-----------------------------------
    @Test
    void getRestaurantMenuByCategory_ValidCategory_ReturnsPageOfMealResponses() {
        IRestaurantRepository restaurantRepository = mock(IRestaurantRepository.class);
        IMealRepository mealRepository = mock(IMealRepository.class);
        MealEntityMapper mealEntityMapper = mock(MealEntityMapper.class);

        MealAdapter mealAdapter = new MealAdapter(mealRepository, mealEntityMapper);

        Long restaurantId = 1L;
        String category = "Main Course";
        int page = 0;
        int size = 10;

        RestaurantEntity restaurant = new RestaurantEntity();
        restaurant.setId(restaurantId);
        when(restaurantRepository.findById(restaurantId)).thenReturn(Optional.of(restaurant));

        List<MealEntity> mealEntities = new ArrayList<>();
        mealEntities.add(new MealEntity());

        Pageable pageable = PageRequest.of(page, size);
        Page<MealEntity> mealPage = new PageImpl<>(mealEntities);
        when(mealRepository.findByRestaurantIdAndCategory(restaurant, category, pageable)).thenReturn(mealPage);

        Page<MealResponse> result = mealAdapter.getRestaurantMenuByCategory(restaurant, category, page, size);

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
    }

    @Test
        void testFindAll_ReturnsPageOfRestaurants() {
        Pageable pageable = PageRequest.of(0, 10);

        List<RestaurantEntity> restaurantEntities = new ArrayList<>();
        restaurantEntities.add(new RestaurantEntity( 1L, "name", 213123, "newsdealer", "+573005698325", "", new UserEntity(), null));

        Page<RestaurantEntity> restaurantEntityPage = new PageImpl<>(restaurantEntities, pageable, restaurantEntities.size());

        when(restaurantRepository.findAll(pageable)).thenReturn(restaurantEntityPage);

        Page<Restaurant> result = restaurantAdapter.findAll(pageable);

        assertEquals(restaurantEntities.size(), result.getContent().size());

    }

    @Test
    void testGetOrdersByStateAndRestaurant() {

        Restaurant restaurant = new Restaurant(1L, "name", 213123, "newsdealer", "+573005698325", "", null, null);
        OrderStatus state = OrderStatus.PENDING;
        Pageable pageable = Pageable.ofSize(10).withPage(0);
        MealEntity meal = new MealEntity();
        List<OrderResponse> orders = new ArrayList<>();
        orders.add(new OrderResponse(1L, new User( 1L, "user", 3265326, "+573226094632", LocalDate.of(1995, 8, 20), "aa@aa.com", "12334", "CLIENT" ), restaurant, List.of(meal), state, 1L, 1, "1234", LocalDateTime.now(),null, 1L));


        Page<OrderResponse> ordersPage = new PageImpl<>(orders, pageable, orders.size());


        when(orderHandler.getOrdersByStateAndRestaurant(state, restaurant, pageable)).thenReturn(ordersPage);


        Page<OrderResponse> result = orderHandler.getOrdersByStateAndRestaurant(state, restaurant, pageable);


        assertEquals(ordersPage.getTotalElements(), result.getTotalElements());

    }



}
