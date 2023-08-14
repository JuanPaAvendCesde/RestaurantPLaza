package org.pragma.restaurantplaza.infrastructure.configuration;

import org.pragma.restaurantplaza.domain.api.IOwnerServicePort;
import org.pragma.restaurantplaza.domain.api.IRestaurantServicePort;
import org.pragma.restaurantplaza.domain.spi.IOwnerPersistencePort;
import org.pragma.restaurantplaza.domain.spi.IRestaurantPersistencePort;
import org.pragma.restaurantplaza.domain.usecase.OwnerUseCase;
import org.pragma.restaurantplaza.domain.usecase.RestaurantUseCase;
import org.pragma.restaurantplaza.infrastructure.output.jpa.adapter.OwnerAdapter;
import org.pragma.restaurantplaza.infrastructure.output.jpa.adapter.RestaurantAdapter;
import org.pragma.restaurantplaza.infrastructure.output.jpa.mapper.OwnerEntityMapper;
import org.pragma.restaurantplaza.infrastructure.output.jpa.mapper.RestaurantEntityMapper;
import org.pragma.restaurantplaza.infrastructure.output.jpa.repository.IOwnerRepository;
import lombok.RequiredArgsConstructor;
import org.pragma.restaurantplaza.infrastructure.output.jpa.repository.IRestaurantRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class BeanConfiguration {

    private final IOwnerRepository ownerRepository;
    private final OwnerEntityMapper ownerEntityMapper;
    private final IRestaurantRepository restaurantRepository;
    private final RestaurantEntityMapper restaurantEntityMapper;

    @Bean
    public IOwnerPersistencePort ownerPersistencePort() {
        return new OwnerAdapter(ownerRepository, ownerEntityMapper);
    }
    @Bean
    public IOwnerServicePort ownerServicePort() {
    return new OwnerUseCase(ownerPersistencePort());
    }

    @Bean
    public IRestaurantPersistencePort restaurantPersistencePort() {
        return new RestaurantAdapter(restaurantRepository, restaurantEntityMapper);
    }
    @Bean
    public IRestaurantServicePort restaurantService() {
        return new RestaurantUseCase(restaurantPersistencePort());
    }

}
