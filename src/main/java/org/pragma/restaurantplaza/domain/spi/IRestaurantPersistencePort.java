package org.pragma.restaurantplaza.domain.spi;

import org.pragma.restaurantplaza.application.dto.RestaurantResponse;
import org.pragma.restaurantplaza.domain.model.Restaurant;
import org.pragma.restaurantplaza.infrastructure.output.jpa.entity.RestaurantEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;


public interface IRestaurantPersistencePort {
    void saveRestaurant(Restaurant restaurant);

    RestaurantResponse mapToRestaurantResponse(RestaurantEntity restaurantEntity);

    RestaurantEntity findById(Long restaurantId);

    /*@Override
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


    RestaurantResponse mapToRestaurantResponse(RestaurantEntity restaurantEntity);


    //crear controla
    /*List<MealResponse> getRestaurantMenu(Long restaurantId);

    Page<MealResponse> getRestaurantMenuByCategory(Long restaurantId, String name, String category, int page, int size);


    Page<Restaurant> findAll(Pageable pageable);

    Page<Order> getOrdersByStateAndRestaurant(OrderStatus state, Restaurant restaurant, Pageable pageable);

    Page<Order> getAssignedOrdersByStateAndRestaurant(OrderStatus state, Long employeeId, Long restaurantId, Pageable pageable);

    */
}
