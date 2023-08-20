package org.pragma.restaurantplaza.application.handler;

import org.pragma.restaurantplaza.application.dto.UserRequest;
import org.pragma.restaurantplaza.application.dto.UserResponse;
import org.pragma.restaurantplaza.application.mapper.OwnerRequestMapper;
import org.pragma.restaurantplaza.application.mapper.OwnerResponseMapper;
import org.pragma.restaurantplaza.domain.api.IUserServicePort;
import org.pragma.restaurantplaza.domain.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Service
@RequiredArgsConstructor
@Transactional
public class UserHandler implements IUserHandler {

    private final IUserServicePort ownerServicePort;
    private final OwnerRequestMapper ownerRequestMapper;
    private final OwnerResponseMapper ownerResponseMapper;
    @Override
    public void saveUser(UserRequest userRequest) {
        User user = ownerRequestMapper.toOwner(userRequest);
        ownerServicePort.saveUser(user);

    }
    @Override
    public List<UserResponse> getAllOwners() {
        return ownerResponseMapper.toResponseList(ownerServicePort.getAllOwners());
    }

    @Override
    public User findById(User userId) {
        return ownerServicePort.findById(userId);
    }

}
