package org.pragma.restaurantplaza.application.handler;

import lombok.RequiredArgsConstructor;
import org.pragma.restaurantplaza.application.dto.UserRequest;
import org.pragma.restaurantplaza.application.mapper.OwnerRequestMapper;
import org.pragma.restaurantplaza.domain.api.IUserServicePort;
import org.pragma.restaurantplaza.domain.model.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserHandler implements IUserHandler {

    private final IUserServicePort ownerServicePort;
    private final OwnerRequestMapper ownerRequestMapper;

    @Override
    public void saveUser(UserRequest userRequest) {
        User user = ownerRequestMapper.toOwner(userRequest);
        ownerServicePort.saveUser(user);
    }

    @Override
    public User findById(User userId) {
        return ownerServicePort.findById(userId);
    }


}
