package org.pragma.restaurantplaza.infrastructure.output.jpa.adapter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.pragma.restaurantplaza.domain.model.User;
import org.pragma.restaurantplaza.infrastructure.exception.*;
import org.pragma.restaurantplaza.infrastructure.output.jpa.entity.UserEntity;
import org.pragma.restaurantplaza.infrastructure.output.jpa.mapper.UserEntityMapper;
import org.pragma.restaurantplaza.infrastructure.output.jpa.repository.IUserRepository;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OwnerAdapterTest {

    @Mock
    private IUserRepository userRepository;

    @Mock
    private UserEntityMapper userEntityMapper;

    private UserAdapter userAdapter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userAdapter = new UserAdapter(userRepository, userEntityMapper);
    }

    @Test
    void saveOwner_SuccessfulCreation() {

        User user = new User(1L, "user", 3265326, "+573226094632", LocalDate.of(1995, 8, 20), "aa@aa.com", "12334", "OWNER");

        when(userRepository.findById(1L)).thenReturn(Optional.empty());
        when(userEntityMapper.toUserEntity(user)).thenReturn(new UserEntity());

        assertDoesNotThrow(() -> userAdapter.saveUser(user));
        verify(userRepository, times(1)).findById(1L);
        verify(userEntityMapper, times(1)).toUserEntity(user);
        verify(userRepository, times(1)).save(any(UserEntity.class));
    }


    @Test
    void saveOwner_AlreadyExists() {

        User owner = new User(1L, "user", 3265326, "+573226094632", LocalDate.of(1995, 7, 8), "aa@aa.com", "12334", "OWNER");

        when(userRepository.findById(1L)).thenReturn(Optional.of(new UserEntity()));

        assertThrows(UserAlreadyExistException.class, () -> userAdapter.saveUser(owner));
        verify(userRepository, times(1)).findById(1L);

    }

    @Test
    void saveOwner_UnderageOwner() {

        User owner = new User(1L, "user", 3265326, "+573226094632", LocalDate.of(2015, 7, 8), "aa@aa.com", "12334", "OWNER");

        owner.setId(2L);
        owner.setBirthdate(LocalDate.now().minusYears(17));

        when(userRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(UserMustBeAdultException.class, () -> userAdapter.saveUser(owner));
        verify(userRepository, times(1)).findById(2L);

    }

    @Test
    void saveUser_InvalidEmailFormat() {

        User user = new User(1L, "user", 3265326, "+573226094632", LocalDate.of(1995, 8, 20), "email_nova-lido", "12334", "OWNER");

        InvalidEmailException exception = assertThrows(InvalidEmailException.class, () -> userAdapter.saveUser(user));
        assertEquals("Invalid email format", exception.getMessage());

    }

    @Test
    void saveUser_InvalidPhoneNumberFormat() {

        User user = new User(1L, "user", 3265326, "45681", LocalDate.of(1995, 8, 20), "email@email.com", "12334", "OWNER");

        InvalidPhoneNumberException exception = assertThrows(InvalidPhoneNumberException.class, () -> userAdapter.saveUser(user));
        assertEquals("Invalid phone format", exception.getMessage());

    }

    @Test
    void saveUser_InvalidDocumentFormat() {
        // Arrange
        User user = new User(1L, "user", -3265326, "+573226094632", LocalDate.of(1995, 8, 20), "email@example.com", "12334", "Owner");

        // Act & Assert
        InvalidDocumentException exception = assertThrows(InvalidDocumentException.class, () -> userAdapter.saveUser(user));
        assertEquals("Invalid document format", exception.getMessage());
    }
}