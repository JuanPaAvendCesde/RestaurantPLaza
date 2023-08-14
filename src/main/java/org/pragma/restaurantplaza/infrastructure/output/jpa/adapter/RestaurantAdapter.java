package org.pragma.restaurantplaza.infrastructure.output.jpa.adapter;

import lombok.RequiredArgsConstructor;
import org.pragma.restaurantplaza.domain.model.Owner;
import org.pragma.restaurantplaza.domain.model.Restaurant;
import org.pragma.restaurantplaza.domain.spi.IRestaurantPersistencePort;

import org.pragma.restaurantplaza.infrastructure.exception.RestaurantAlreadyExistException;
import org.pragma.restaurantplaza.infrastructure.output.jpa.mapper.RestaurantEntityMapper;
import org.pragma.restaurantplaza.infrastructure.output.jpa.repository.IRestaurantRepository;

@RequiredArgsConstructor
public class RestaurantAdapter implements IRestaurantPersistencePort {

    private final IRestaurantRepository restaurantRepository;
    private final RestaurantEntityMapper restaurantEntityMapper;
    @Override
    public void saveRestaurant(Restaurant restaurant, Owner owner) {
        if(restaurantRepository.findById(restaurant.getId()).isPresent() ) {
            throw new RestaurantAlreadyExistException("Restaurant already exists");
        }
        restaurantRepository.save(restaurantEntityMapper.toRestaurantEntity(restaurant));
    }
}
