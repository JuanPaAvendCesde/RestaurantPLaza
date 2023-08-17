package org.pragma.restaurantplaza.domain.usecase;

import org.pragma.restaurantplaza.domain.api.IUserServicePort;
import org.pragma.restaurantplaza.domain.model.User;
import org.pragma.restaurantplaza.domain.spi.IUserPersistencePort;

import java.util.List;

public class UserUseCase implements IUserServicePort {

    private final IUserPersistencePort userPersistencePort;

    public UserUseCase(IUserPersistencePort userPersistencePort) {
        this.userPersistencePort = userPersistencePort;
    }

    @Override
    public void saveUser(User user) {
    userPersistencePort.saveUser(user);
    }

    @Override
    public List<User> getAllOwners() {
        return userPersistencePort.getAllOwners();
    }

    @Override
    public User findById(User userId) {
        return userPersistencePort.findById(userId);
    }
}
