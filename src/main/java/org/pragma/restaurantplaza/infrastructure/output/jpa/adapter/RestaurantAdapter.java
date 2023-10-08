package org.pragma.restaurantplaza.infrastructure.output.jpa.adapter;

import lombok.RequiredArgsConstructor;
import org.pragma.restaurantplaza.application.dto.RestaurantResponse;
import org.pragma.restaurantplaza.domain.model.Restaurant;
import org.pragma.restaurantplaza.domain.spi.IRestaurantPersistencePort;
import org.pragma.restaurantplaza.infrastructure.exception.*;
import org.pragma.restaurantplaza.infrastructure.output.jpa.entity.RestaurantEntity;
import org.pragma.restaurantplaza.infrastructure.output.jpa.entity.UserEntity;
import org.pragma.restaurantplaza.infrastructure.output.jpa.mapper.RestaurantEntityMapper;
import org.pragma.restaurantplaza.infrastructure.output.jpa.repository.IMealRepository;
import org.pragma.restaurantplaza.infrastructure.output.jpa.repository.IOrderRepository;
import org.pragma.restaurantplaza.infrastructure.output.jpa.repository.IRestaurantRepository;
import org.pragma.restaurantplaza.infrastructure.output.jpa.repository.IUserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


@RequiredArgsConstructor
public class RestaurantAdapter implements IRestaurantPersistencePort {

    private final IRestaurantRepository restaurantRepository;
    private final RestaurantEntityMapper restaurantEntityMapper;
    private final IMealRepository mealRepository;
    private final IOrderRepository orderRepository;
    private final IUserRepository userRepository;


    String restaurantNotFound = "Restaurant not found";


    @Override
    public void saveRestaurant(Restaurant restaurant) {


        if (restaurantRepository.findById(restaurant.getId()).isPresent()) {
            throw new RestaurantAlreadyExistException("Restaurant already exists");
        }

        if (containsOnlyNumbers(restaurant.getName())) {
            throw new InvalidRestaurantNameException("Restaurant name cannot consist of only numbers");
        }

        if ((restaurant.getNit()) < 0) {
            throw new InvalidNitFormatException("Invalid NIT format");
        }
        if (!isValidPhoneNumber(restaurant.getPhone())) {
            throw new InvalidPhoneNumberException("Invalid restaurant phone format");
        }
        UserEntity user = userRepository.findById(restaurant.getOwnerId().getId())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        if (user.getRole() != null && !user.getRole().equals("OWNER")) {
            throw new InvalidUserRoleException("User must have the 'Owner' role to create a restaurant");
        }

        restaurantRepository.save(restaurantEntityMapper.toRestaurantEntity(restaurant));
    }

    private boolean containsOnlyNumbers(String input) {
        return input.matches("^\\d+$");
    }

    /* @Override
     public List<MealResponse> getRestaurantMenu(Long restaurantId) {
         RestaurantEntity restaurant = restaurantRepository.findById(restaurantId)
                 .orElseThrow(() -> new RestaurantNotFoundException(restaurantNotFound));
         List<MealResponse> mealResponses = new ArrayList<>();
         for (MealEntity meal : restaurant.getMeals()) {
             mealResponses.add(mapToMealResponse(meal));
         }
         return mealResponses;
     }
 */
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















    public Page<RestaurantResponse> getAllRestaurantsOrderedByName(Pageable pageable) {
        Page<RestaurantEntity> restaurantEntityPage = restaurantRepository.findAllByOrderByNameAsc(pageable);
        return restaurantEntityPage.map(this::mapToRestaurantResponse);
    }

    @Override
    public RestaurantResponse mapToRestaurantResponse(RestaurantEntity restaurantEntity) {
        RestaurantResponse restaurantResponse = new RestaurantResponse();
        restaurantResponse.setId(restaurantEntity.getId());
        restaurantResponse.setName(restaurantEntity.getName());
        restaurantResponse.setUrlLogo(restaurantEntity.getUrlLogo());

        return restaurantResponse;
    }

    @Override
    public RestaurantEntity findById(Long restaurantId) {
        return restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new EntityNotFoundException("Restaurant not found"));
    }


}
