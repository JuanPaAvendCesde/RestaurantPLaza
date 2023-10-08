package org.pragma.restaurantplaza.adapterTests;

import com.twilio.Twilio;
import com.twilio.twiml.messaging.Message;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.pragma.restaurantplaza.application.dto.MealRequest;
import org.pragma.restaurantplaza.application.dto.OrderRequest;
import org.pragma.restaurantplaza.application.dto.UserResponse;
import org.pragma.restaurantplaza.domain.model.Meal;
import org.pragma.restaurantplaza.domain.model.OrderStatus;
import org.pragma.restaurantplaza.domain.model.Restaurant;
import org.pragma.restaurantplaza.domain.model.User;
import org.pragma.restaurantplaza.infrastructure.exception.*;
import org.pragma.restaurantplaza.infrastructure.output.jpa.adapter.MealAdapter;
import org.pragma.restaurantplaza.infrastructure.output.jpa.adapter.OrderAdapter;
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

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OwnerAdapterTest {

    private static final String ACCOUNT_SID = "AC62c10541448481c0bcc69e916652a4e4";
    private static final String AUTH_TOKEN = "b486013694652cdc5d1088bbd2db594b";
    private static final String TWILIO_PHONE_NUMBER= "+14788224490";
    @Mock
    private IMealRepository mealRepository;

    @Mock
    private MealEntityMapper mealEntityMapper;

    private MealAdapter mealAdapter;
    private UserAdapter userAdapter;

    private OrderAdapter orderAdapter;


    @Mock
    private UserEntityMapper userEntityMapper;

    @Mock
    private IUserRepository userRepository;

    @Mock
    private RestaurantEntityMapper restaurantEntityMapper;

    @Mock
    private OrderEntityMapper orderEntityMapper;

    @Mock
    private IOrderRepository orderRepository;

    @Mock
    private IRestaurantRepository restaurantRepository;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mealAdapter = new MealAdapter(mealRepository, mealEntityMapper, restaurantRepository);
        userAdapter = new UserAdapter(userRepository, userEntityMapper);
        orderAdapter= new OrderAdapter(orderRepository, orderEntityMapper, userRepository, mealEntityMapper, userEntityMapper, restaurantEntityMapper,restaurantRepository,mealRepository);
    }

//historia3--------------------------------------------------------------------
@Test
void saveMeal_SuccessfulCreation() {
    Restaurant restaurant = new Restaurant( 1L, "name", 213123, "rewrite", "+573005698325", "", null);
    MealRequest mealRequest = new MealRequest(1L, "Sombre de la comical", 123, "Description", "Ingredients", "URL de la image", restaurant);

    when(mealRepository.findById(1L)).thenReturn(Optional.empty());
    when(restaurantRepository.findById(1L)).thenReturn(Optional.of(new RestaurantEntity()));
    when(mealEntityMapper.toMealEntity(mealRequest)).thenReturn(new MealEntity());

    assertDoesNotThrow(() -> mealAdapter.saveMeal(mealRequest));

    verify(mealRepository, times(1)).findById(1L);
    verify(mealRepository, times(1)).save(any(MealEntity.class));
}

  /*  @Test
    void saveMeal_UnsuccessfulCreation() {
        UserEntity userEntity = new UserEntity();
        userEntity.setRole("CLIENT");
        User user = new User(1L, "user", 3265326, "+573226094632", LocalDate.of(1995, 8, 20), "aa@aa.com", "12334", "CLIENT");
        Restaurant restaurant = new Restaurant(1L, "name", 213123, "rewrite", "+573005698325", "", user);

        MealRequest meal = new MealRequest(1L, "Sombre de la comical", 123, "Description", "Ingredients", "URL de la image", restaurant);
        MealEntity mealEntity = new MealEntity();
        mealEntity.setRestaurantId(new RestaurantEntity());


        when(mealEntityMapper.toMealEntity(meal)).thenReturn(mealEntity);

        InvalidUserRoleException exception = assertThrows(
                InvalidUserRoleException.class,
                () -> mealAdapter.saveMeal(meal)
        );
        assertEquals("User must have the 'Owner' role to create a meal", exception.getMessage());
        verify(mealRepository, times(1)).findById(1L);
    }*/

    @Test
    void saveMeal_AlreadyExists() {
        User user = new User(1L, "user", 3265326, "+573226094632", LocalDate.of(1995, 8, 20), "aa@aa.com", "12334", "OWNER");
        Restaurant restaurant = new Restaurant(1L, "name", 213123, "rewrite", "+573005698325", "", user);
        MealRequest meal = new MealRequest(1L, "Sombre de la comical", 123, "Description", "Ingredients", "URL de la image", restaurant);

        when(mealRepository.findById(1L)).thenReturn(Optional.of(new MealEntity()));

        assertThrows(UserAlreadyExistException.class, () -> mealAdapter.saveMeal(meal));
        verify(mealRepository, times(1)).findById(1L);
        verifyNoMoreInteractions(mealEntityMapper);
        verifyNoMoreInteractions(mealRepository);
    }


    @Test
    void saveMeal_InvalidMealPrice() {

        User user = new User(1L, "user", 3265326, "+573226094632", LocalDate.of(1995, 8, 20), "aa@aa.com", "12334", "OWNER");
        Restaurant restaurant = new Restaurant(1L, "name", 213123, "rewrite", "+573005698325", "", user);
        MealRequest meal = new MealRequest(1L, "Sombre de la comical", -65123, "Description", "Ingredients", "URL de la image", restaurant);

        when(mealRepository.findById(user.getId())).thenReturn(Optional.empty());
        InvalidMealPriceException exception = assertThrows(
                InvalidMealPriceException.class,
                () -> mealAdapter.saveMeal(meal)
        );

        assertEquals("Meal price must be a positive number greater than 0", exception.getMessage());
        verify(mealRepository, times(1)).findById(user.getId());
    }

    //historia4--------------------------------------------------------------------
/*
    @Test
    void updateMeal_SuccessfulUpdate() {
        long mealId = 1L;
        int newPrice = 12345;
        String newDescription = "New description";
        MealEntity existingMealEntity = new MealEntity();
        existingMealEntity.setPrice(100);
        existingMealEntity.setDescription("Old description");
        existingMealEntity.setRestaurantId(new RestaurantEntity());
        existingMealEntity.setRestaurantId().getUserId(new UserEntity());
        existingMealEntity.setRestaurantId().getUserId().setRole("OWNER");

        when(mealRepository.findById(mealId)).thenReturn(Optional.of(existingMealEntity));

        mealAdapter.updateMeal(mealId, newPrice, newDescription);

        verify(mealRepository, times(1)).findById(mealId);
        verify(mealRepository, times(1)).save(existingMealEntity);
        assertEquals(newPrice, existingMealEntity.getPrice());
        assertEquals(newDescription, existingMealEntity.getDescription());
    }

    @Test
    void updateMeal_UnsuccessfulUpdate() {

        long mealId = 1L;
        int newPrice = 12345;
        String newDescription = "New description";
        MealEntity existingMealEntity = new MealEntity();
        existingMealEntity.setPrice(100);
        existingMealEntity.setDescription("Old description");
        existingMealEntity.setRestaurantId(new RestaurantEntity());
        existingMealEntity.getRestaurantId().setUserId(new UserEntity());
        existingMealEntity.getRestaurantId().getUserId().setRole("USER");

        when(mealRepository.findById(mealId)).thenReturn(Optional.of(existingMealEntity));

        InvalidUserRoleException exception = assertThrows(
                InvalidUserRoleException.class,
                () -> mealAdapter.updateMeal(mealId, newPrice, newDescription)
        );

        assertEquals("User must have the 'Owner' role to update a meal", exception.getMessage());
        verify(mealRepository, times(1)).findById(mealId);
        verify(mealRepository, times(0)).save(existingMealEntity);
    }

    //historia6--------------------------------------------------------------------
    @Test
    void saveEmployee_SuccessfulCreation() {

        User user = new User(1L, "user", 3265326, "+573226094632", LocalDate.of(1995, 8, 20), "aa@aa.com", "12334", "Employee");

        when(userRepository.findById(1L)).thenReturn(Optional.empty());
        when(userEntityMapper.toUserEntity(user)).thenReturn(new UserEntity());

        assertDoesNotThrow(() -> userAdapter.saveUser(user));
        verify(userRepository, times(1)).findById(1L);
        verify(userEntityMapper, times(1)).toUserEntity(user);
        verify(userRepository, times(1)).save(any(UserEntity.class));
    }

    @Test
    void saveEmployee_AlreadyExists() {

        User owner = new User(1L, "user", 3265326, "+573226094632", LocalDate.of(1995, 7, 8), "aa@aa.com", "12334", "Employee");

        when(userRepository.findById(1L)).thenReturn(Optional.of(new UserEntity()));

        assertThrows(UserAlreadyExistException.class, () -> userAdapter.saveUser(owner));
        verify(userRepository, times(1)).findById(1L);

    }

    //historia7--------------------------------------------------------------------
 /*   @Test
    void changeMealStatus_ValidMealIdAndRole_ActiveStatusChanged() {

        Long mealId = 1L;
        boolean active = true;
        User owner = new User(1L, "user", 3265326, "+573226094632", LocalDate.of(1995, 8, 20), "aa@aa.com", "12334", "OWNER");
        owner.setRole("OWNER");
        MealEntity existingMealEntity = new MealEntity();
        existingMealEntity.setId(mealId);
        existingMealEntity.setActive(!active);
        RestaurantEntity restaurantEntity = new RestaurantEntity();
        restaurantEntity.setUserId(new UserEntity());
        restaurantEntity.getUserId().setRole("OWNER");
        existingMealEntity.setRestaurantId(restaurantEntity);

        when(mealRepository.findById(mealId)).thenReturn(Optional.of(existingMealEntity));
        MealAdapter mealAdapter = new MealAdapter(mealRepository, mealEntityMapper);
        assertDoesNotThrow(() -> mealAdapter.changeMealStatus(mealId, active));

        assertTrue(existingMealEntity.isActive());
        verify(mealRepository, times(1)).findById(mealId);
        verify(mealRepository, times(1)).save(existingMealEntity);
    }

    @Test
    void changeMealStatus_InvalidRole_ThrowsInvalidUserRoleException() {

        Long mealId = 1L;
        boolean active = true;
        MealEntity existingMealEntity = new MealEntity();
        existingMealEntity.setId(mealId);
        RestaurantEntity restaurantEntity = new RestaurantEntity();
        UserEntity userEntity = new UserEntity();
        userEntity.setRole("Client");
        restaurantEntity.setUserId(userEntity);
        existingMealEntity.setRestaurantId(restaurantEntity);

        when(mealRepository.findById(mealId)).thenReturn(Optional.of(existingMealEntity));
        MealAdapter mealAdapter = new MealAdapter(mealRepository, mealEntityMapper);
        InvalidUserRoleException exception = assertThrows(
                InvalidUserRoleException.class,
                () -> mealAdapter.changeMealStatus(mealId, active)
        );

        assertEquals("Invalid user role", exception.getMessage());
    }

    @Test
    void changeMealStatus_MealNotFound_ThrowsMealNotFoundException() {

        Long mealId = 1L;
        boolean active = true;

        when(mealRepository.findById(mealId)).thenReturn(Optional.empty());
        MealAdapter mealAdapter = new MealAdapter(mealRepository, mealEntityMapper);

        assertThrows(MealNotFoundException.class, () -> mealAdapter.changeMealStatus(mealId, active));
        verify(mealRepository, times(1)).findById(mealId);
        verify(mealRepository, times(0)).save(any(MealEntity.class));
    }
    @Test
    void sendOrderNotification_ValidInput_SendsMessage() {
        // Arrange
        UserEntity userEntity = new UserEntity();
        userEntity.setPhone("+573226094632");
        User user = userAdapter.mapToUser(userEntity);
        String pin = "1234";

        // Create mock data for OrderRequest
        OrderRequest orderRequest = new OrderRequest(
                1L,
                user,
                new Restaurant(1L, "name", 213123, "rewrite", "+573005698325", "", null, null),
                new ArrayList<>(),
                OrderStatus.READY,
                1L,
                1,
                "1234",
                LocalDateTime.now().minusHours(1),
                LocalDateTime.now(),
                60
        );

        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
        Message.Builder creator = mock(Message.Builder.class);
        when(creator.build()).thenReturn(mock(Message.class));
        doReturn(creator).when(orderAdapter).sendOrderNotification(any(UserEntity.class), anyString(), any(OrderRequest.class));


        // Act
        orderAdapter.sendOrderNotification(userEntity, pin, orderRequest);

        // Assert
        verify(orderAdapter, times(1)).sendOrderNotification(userEntity, pin, orderRequest);
        verify(creator, times(1)).build();
    }

    //Historia18--------------------------------------------------------------------

    @Test
    void calculateEmployeeEfficiency() {
        // Arrange
        UserEntity employee = new UserEntity();
        employee.setId(1L);
        employee.setRole("EMPLOYEE");

        LocalDateTime startTime = LocalDateTime.of(2023, 8, 13, 12, 0);
        LocalDateTime endTime = LocalDateTime.of(2023, 8, 13, 12, 30);
        long totalDuration = ChronoUnit.MINUTES.between(startTime, endTime);

        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setId(1L);
        orderEntity.setCreateAt(startTime);
        orderEntity.setUpdateAt(endTime);
        orderEntity.setAssignedEmployeeId(employee.getId());
        orderEntity.setOrderStatus(OrderStatus.DELIVERED);

        List<UserEntity> employees = new ArrayList<>();
        employees.add(employee);

        List<OrderEntity> ordersHandledByEmployee = new ArrayList<>();
        ordersHandledByEmployee.add(orderEntity);

        when(userRepository.findByRole("EMPLOYEE")).thenReturn(employees);
        when(orderRepository.findByAssignedEmployeeIdAndOrderStatus(employee.getId(), OrderStatus.DELIVERED)).thenReturn(ordersHandledByEmployee);

        // Act
        List<UserResponse> employeeEfficiencyList = orderAdapter.calculateEmployeeEfficiency();

        // Assert
        assertEquals(1, employeeEfficiencyList.size());
        UserResponse employeeEfficiency = employeeEfficiencyList.get(0);
        assertEquals(1L, employeeEfficiency.getId());
        assertEquals(totalDuration, employeeEfficiency.getEmployeeRecord());
        verify(userRepository, times(1)).findByRole("EMPLOYEE");
        verify(orderRepository, times(1)).findByAssignedEmployeeIdAndOrderStatus(employee.getId(), OrderStatus.DELIVERED);
    }*/





}