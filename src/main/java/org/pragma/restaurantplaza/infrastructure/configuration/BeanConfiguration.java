package org.pragma.restaurantplaza.infrastructure.configuration;

import lombok.RequiredArgsConstructor;
import org.pragma.restaurantplaza.domain.api.IMealServicePort;
import org.pragma.restaurantplaza.domain.api.IRestaurantServicePort;
import org.pragma.restaurantplaza.domain.api.IUserServicePort;
import org.pragma.restaurantplaza.domain.spi.IMealPersistencePort;
import org.pragma.restaurantplaza.domain.spi.IRestaurantPersistencePort;
import org.pragma.restaurantplaza.domain.spi.IUserPersistencePort;
import org.pragma.restaurantplaza.domain.usecase.MealUseCase;
import org.pragma.restaurantplaza.domain.usecase.RestaurantUseCase;
import org.pragma.restaurantplaza.domain.usecase.UserUseCase;
import org.pragma.restaurantplaza.infrastructure.output.jpa.adapter.MealAdapter;
import org.pragma.restaurantplaza.infrastructure.output.jpa.adapter.RestaurantAdapter;
import org.pragma.restaurantplaza.infrastructure.output.jpa.adapter.UserAdapter;
import org.pragma.restaurantplaza.infrastructure.output.jpa.mapper.MealEntityMapper;
import org.pragma.restaurantplaza.infrastructure.output.jpa.mapper.RestaurantEntityMapper;
import org.pragma.restaurantplaza.infrastructure.output.jpa.mapper.UserEntityMapper;
import org.pragma.restaurantplaza.infrastructure.output.jpa.repository.IMealRepository;
import org.pragma.restaurantplaza.infrastructure.output.jpa.repository.IOrderRepository;
import org.pragma.restaurantplaza.infrastructure.output.jpa.repository.IRestaurantRepository;
import org.pragma.restaurantplaza.infrastructure.output.jpa.repository.IUserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class BeanConfiguration {

    private final IUserRepository userRepository;
    private final UserEntityMapper userEntityMapper;
    private final IRestaurantRepository restaurantRepository;
    private final RestaurantEntityMapper restaurantEntityMapper;
    private final IMealRepository mealRepository;
    private final MealEntityMapper mealEntityMapper;
    private final IOrderRepository orderRepository;




    @Bean
    public IUserPersistencePort userPersistencePort() {
        return new UserAdapter(userRepository, userEntityMapper);
    }

    @Bean
    public IUserServicePort userServicePort() {
        return new UserUseCase(userPersistencePort());
    }

    @Bean
    public IRestaurantPersistencePort restaurantPersistencePort() {
        return new RestaurantAdapter(restaurantRepository, restaurantEntityMapper, mealRepository,orderRepository);
    }

    @Bean
    public IRestaurantServicePort restaurantService() {
        return new RestaurantUseCase(restaurantPersistencePort());
    }

    @Bean
    public IMealPersistencePort mealPersistencePort() {
        return new MealAdapter(mealRepository, mealEntityMapper);
    }

    @Bean
    public IMealServicePort mealServicePort() {
        return new MealUseCase(mealPersistencePort());
    }
}
