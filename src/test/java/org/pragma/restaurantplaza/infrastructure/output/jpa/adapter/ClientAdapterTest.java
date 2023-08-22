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

class ClientAdapterTest {

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
    void saveClient_SuccessfulCreation() {

        User user = new User(1L, "user", 3265326, "+573226094632", LocalDate.of(2015, 8, 20), "aa@aa.com", "12334", "CLIENT");

        when(userRepository.findById(1L)).thenReturn(Optional.empty());
        when(userEntityMapper.toUserEntity(user)).thenReturn(new UserEntity());

        assertDoesNotThrow(() -> userAdapter.saveUser(user));
        verify(userRepository, times(1)).findById(1L);
        verify(userEntityMapper, times(1)).toUserEntity(user);
        verify(userRepository, times(1)).save(any(UserEntity.class));
    }

    @Test
    void saveClient_AlreadyExists() {

        User owner = new User(1L, "user", 3265326, "+573226094632", LocalDate.of(1995, 7, 8), "aa@aa.com", "12334", "CLIENT");

        when(userRepository.findById(1L)).thenReturn(Optional.of(new UserEntity()));

        assertThrows(UserAlreadyExistException.class, () -> userAdapter.saveUser(owner));
        verify(userRepository, times(1)).findById(1L);

    }
}