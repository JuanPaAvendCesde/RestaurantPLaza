package com.example.RestaurantPLaza.infrastructure.configuration;

import com.example.RestaurantPLaza.domain.api.IOwnerServicePort;
import com.example.RestaurantPLaza.domain.spi.IOwnerPersistencePort;
import com.example.RestaurantPLaza.domain.useCase.OwnerUseCase;
import com.example.RestaurantPLaza.infrastructure.output.jpa.adapter.OwnerAdapter;
import com.example.RestaurantPLaza.infrastructure.output.jpa.mapper.OwnerEntityMapper;
import com.example.RestaurantPLaza.infrastructure.output.jpa.repository.IOwnerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class BeanConfiguration {

    private final IOwnerRepository ownerRepository;
    private final OwnerEntityMapper ownerEntityMapper;

    @Bean
    public IOwnerPersistencePort ownerPersistencePort() {
        return new OwnerAdapter(ownerRepository, ownerEntityMapper);
    }
    @Bean
    public IOwnerServicePort ownerServicePort() {
    return new OwnerUseCase(ownerPersistencePort());
    }




}
