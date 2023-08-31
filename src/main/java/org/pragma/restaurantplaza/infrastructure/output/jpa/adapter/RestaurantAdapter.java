package org.pragma.restaurantplaza.infrastructure.output.jpa.adapter;

import lombok.RequiredArgsConstructor;
import org.pragma.restaurantplaza.application.dto.MealResponse;
import org.pragma.restaurantplaza.domain.model.Order;
import org.pragma.restaurantplaza.domain.model.OrderStatus;
import org.pragma.restaurantplaza.domain.model.Restaurant;
import org.pragma.restaurantplaza.domain.model.User;
import org.pragma.restaurantplaza.domain.spi.IRestaurantPersistencePort;
import org.pragma.restaurantplaza.infrastructure.exception.*;
import org.pragma.restaurantplaza.infrastructure.output.jpa.entity.MealEntity;
import org.pragma.restaurantplaza.infrastructure.output.jpa.entity.RestaurantEntity;
import org.pragma.restaurantplaza.infrastructure.output.jpa.mapper.RestaurantEntityMapper;
import org.pragma.restaurantplaza.infrastructure.output.jpa.repository.IMealRepository;
import org.pragma.restaurantplaza.infrastructure.output.jpa.repository.IOrderRepository;
import org.pragma.restaurantplaza.infrastructure.output.jpa.repository.IRestaurantRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import java.util.ArrayList;
import java.util.List;



@RequiredArgsConstructor
public class RestaurantAdapter implements IRestaurantPersistencePort {

    private final IRestaurantRepository restaurantRepository;
    private final RestaurantEntityMapper restaurantEntityMapper;
    private final IMealRepository mealRepository;
    private final IOrderRepository orderRepository;


    String restaurantNotFound = "Restaurant not found";


    @Override
    public void saveRestaurant(Restaurant restaurant, User user) {
        if (restaurantRepository.findById(restaurant.getId()).isPresent()) {
            throw new RestaurantAlreadyExistException("Restaurant already exists");
        }
        if (!user.getRole().equals("OWNER")) {
            throw new InvalidUserRoleException("User must have the 'Owner' role to create a restaurant");
        }
        if ((restaurant.getNit()) < 0) {
            throw new InvalidNitFormatException("Invalid NIT format");
        }
        if (!isValidPhoneNumber(restaurant.getPhone())) {
            throw new InvalidPhoneNumberException("Invalid restaurant phone format");
        }
        if (!restaurant.getName().matches("^[A-Za-z]+$")) {
            throw new InvalidRestaurantNameException("Restaurant name cannot consist of only numbers");
        }
        restaurantRepository.save(restaurantEntityMapper.toRestaurantEntity(restaurant));
    }
    @Override
    public List<MealResponse> getRestaurantMenu(Long restaurantId) {
        RestaurantEntity restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new RestaurantNotFoundException(restaurantNotFound));
        List<MealResponse> mealResponses = new ArrayList<>();
        for (MealEntity meal : restaurant.getMeals()) {
            mealResponses.add(mapToMealResponse(meal));
        }
        return mealResponses;
    }

    private boolean isValidPhoneNumber(String phoneNumber) {

        if (phoneNumber.length() > 13) {
            return false;
        }
        if (!phoneNumber.startsWith("+")) {
            return false;
        }
        for (int i = 1; i < phoneNumber.length(); i++) {
            if (!Character.isDigit(phoneNumber.charAt(i))) {
                return false;
            }
        }
        return true;
    }


@Override
    public Page<MealResponse> getRestaurantMenuByCategory(Long restaurantId,String name, String category, int page, int size) {
        RestaurantEntity restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new RestaurantNotFoundException(restaurantNotFound));

        Pageable pageable = PageRequest.of(page, size);
        Page<MealEntity> mealPage;
        if (category != null) {
            mealPage = mealRepository.findByRestaurantIdAndCategory(restaurant,category, pageable);
        } else {
            mealPage = mealRepository.findByRestaurantId(restaurant, pageable);
        }
        return mealPage.map(this::mapToMealResponse);
    }

@Override
    public Page<Restaurant> findAll(Pageable pageable) {
        Page<RestaurantEntity> restaurantEntityPage = restaurantRepository.findAll(pageable);

        List<Restaurant> restaurantList = restaurantEntityPage.getContent().stream()
                .map(this::mapToRestaurant)
                .toList();

        return new PageImpl<>(restaurantList, pageable, restaurantEntityPage.getTotalElements());
    }

    private Restaurant mapToRestaurant(RestaurantEntity restaurantEntity) {
        User user = new User( restaurantEntity.getUserId().getId(), restaurantEntity.getUserId().getName(), restaurantEntity.getUserId().getDocument(), restaurantEntity.getUserId().getPhone(), restaurantEntity.getUserId().getBirthdate(), restaurantEntity.getUserId().getEmail(), restaurantEntity.getUserId().getPassword(), restaurantEntity.getUserId().getRole());
        return new Restaurant( restaurantEntity.getId(), restaurantEntity.getName(), restaurantEntity.getNit(), restaurantEntity.getUrlLogo(), restaurantEntity.getPhone(), restaurantEntity.getAddress(), user, restaurantEntity.getMeals());
    }


    private MealResponse mapToMealResponse(MealEntity mealEntity) {
        return new MealResponse( mealEntity.getName(), mealEntity.getPrice(),mealEntity.getDescription(), mealEntity.getUrlImage(), mealEntity.getCategory());
    }

    @Override
    public Page<Order> getOrdersByStateAndRestaurant(OrderStatus state, Restaurant restaurant, Pageable pageable) {
        return orderRepository.findByOrderStatusAndRestaurant(state, restaurant, pageable);
    }
    @Override
    public Page<Order> getAssignedOrdersByStateAndRestaurant(OrderStatus state, Long employeeId, Long restaurantId, Pageable pageable) {

        RestaurantEntity restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new EntityNotFoundException(restaurantNotFound));

        return orderRepository.findByOrderStatusAndAssignedEmployeeIdAndRestaurant(state, employeeId, restaurant, pageable);
    }

}
