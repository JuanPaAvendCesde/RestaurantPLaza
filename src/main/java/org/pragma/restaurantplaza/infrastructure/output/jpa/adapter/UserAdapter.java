package org.pragma.restaurantplaza.infrastructure.output.jpa.adapter;

import lombok.RequiredArgsConstructor;
import org.pragma.restaurantplaza.domain.model.User;
import org.pragma.restaurantplaza.domain.spi.IUserPersistencePort;
import org.pragma.restaurantplaza.infrastructure.exception.*;
import org.pragma.restaurantplaza.infrastructure.output.jpa.entity.UserEntity;
import org.pragma.restaurantplaza.infrastructure.output.jpa.mapper.UserEntityMapper;
import org.pragma.restaurantplaza.infrastructure.output.jpa.repository.IUserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDate;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RequiredArgsConstructor
public class UserAdapter implements IUserPersistencePort {

    private final IUserRepository userRepository;
    private final UserEntityMapper userEntityMapper;

    @Override
    public void saveUser(User user) {

        if (!isValidEmail(user.getEmail())) {
            throw new InvalidEmailException("Invalid email format");
        }
        if (!isValidPhoneNumber(user.getPhone())) {
            throw new InvalidPhoneNumberException("Invalid phone format");
        }
        if (userRepository.findById(user.getId()).isPresent()) {
            throw new UserAlreadyExistException("User already exists");
        }
        LocalDate birthdate = user.getBirthdate();
        LocalDate age = LocalDate.now().minusYears(18);
        if (birthdate.isAfter(age) && ("OWNER".equals(user.getRole()))) {
            throw new UserMustBeAdultException("User must be over 18 years old");
        }
        if (!isNumeric(user.getDocument())) {
            throw new InvalidDocumentException("Invalid document format");
        }

        String passEncrypted = passWithBcrypt(user.getPassword());
        user.setPassword(passEncrypted);
        userRepository.save(userEntityMapper.toUserEntity(user));
    }

    private String passWithBcrypt(String pass) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder.encode(pass);
    }

    @Override
    public List<User> getAllOwners() {
        List<UserEntity> userEntityList = userRepository.findAll();

        return userEntityMapper.toUserList(userEntityList);
    }

    @Override
    public User findById(User userId) {
        UserEntity userEntity = userRepository.findById(userId.getId()).orElseThrow();
        return userEntityMapper.toUser(userEntity);
    }


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

    private boolean isValidEmail(String email) {
        String regex = "^[A-Za-z0-9+_.-]+@(.+)$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    private boolean isNumeric(int number) {
        return number >= 0;
    }


}
