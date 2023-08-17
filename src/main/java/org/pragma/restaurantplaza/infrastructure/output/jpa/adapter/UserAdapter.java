package org.pragma.restaurantplaza.infrastructure.output.jpa.adapter;

import org.pragma.restaurantplaza.domain.model.User;
import org.pragma.restaurantplaza.domain.spi.IUserPersistencePort;
import org.pragma.restaurantplaza.infrastructure.exception.UserAlreadyExistException;
import org.pragma.restaurantplaza.infrastructure.exception.UserMustBeAdultException;
import org.pragma.restaurantplaza.infrastructure.output.jpa.entity.UserEntity;
import org.pragma.restaurantplaza.infrastructure.output.jpa.mapper.UserEntityMapper;
import org.pragma.restaurantplaza.infrastructure.output.jpa.repository.IUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDate;
import java.util.List;
@RequiredArgsConstructor
public class UserAdapter implements IUserPersistencePort {

   private final IUserRepository ownerRepository;
   private final UserEntityMapper userEntityMapper;


    @Override
    public void saveUser(User user) {

        if(ownerRepository.findById(user.getId()).isPresent() ) {
            throw new UserAlreadyExistException("User already exists");
        }
        LocalDate birthdate = user.getBirthdate();
        LocalDate age = LocalDate.now().minusYears(18);
        if (birthdate.isAfter(age)) {
            throw new UserMustBeAdultException("User must be over 18 years old");
        }
        String passEncrypted = passWithBcrypt(user.getPassword());
        user.setPassword(passEncrypted);
        ownerRepository.save(userEntityMapper.toOwnerEntity(user));
    }
    private String passWithBcrypt(String pass) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder.encode(pass);
    }
    @Override
    public List<User> getAllOwners() {
       List<UserEntity> userEntityList = ownerRepository.findAll();

       return userEntityMapper.toOwnerList(userEntityList);
    }

    @Override
    public User findById(User userId) {
        UserEntity userEntity = ownerRepository.findById(userId.getId()).orElseThrow();
        return userEntityMapper.toOwner(userEntity);
    }
}
