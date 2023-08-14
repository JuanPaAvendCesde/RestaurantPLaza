package org.pragma.restaurantplaza.infrastructure.configuration;

import org.pragma.restaurantplaza.domain.api.IOwnerServicePort;
import org.pragma.restaurantplaza.domain.spi.IOwnerPersistencePort;
import org.pragma.restaurantplaza.domain.usecase.OwnerUseCase;
import org.pragma.restaurantplaza.infrastructure.output.jpa.adapter.OwnerAdapter;
import org.pragma.restaurantplaza.infrastructure.output.jpa.mapper.OwnerEntityMapper;
import org.pragma.restaurantplaza.infrastructure.output.jpa.repository.IOwnerRepository;
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
