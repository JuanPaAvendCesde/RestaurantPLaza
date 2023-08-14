package org.pragma.restaurantplaza.infrastructure.output.jpa.adapter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.pragma.restaurantplaza.domain.model.Owner;
import org.pragma.restaurantplaza.infrastructure.exception.InvalidEmailException;
import org.pragma.restaurantplaza.infrastructure.exception.OwnerAlreadyExistException;
import org.pragma.restaurantplaza.infrastructure.exception.OwnerMustBeAdultException;
import org.pragma.restaurantplaza.infrastructure.output.jpa.entity.OwnerEntity;
import org.pragma.restaurantplaza.infrastructure.output.jpa.mapper.OwnerEntityMapper;
import org.pragma.restaurantplaza.infrastructure.output.jpa.repository.IOwnerRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OwnerAdapterTest {

    @Mock
    private IOwnerRepository ownerRepository;

    @Mock
    private OwnerEntityMapper ownerEntityMapper;

    private OwnerAdapter ownerAdapter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        ownerAdapter = new OwnerAdapter(ownerRepository, ownerEntityMapper);
    }

    @Test
    void saveOwner_SuccessfulCreation() {
        // Arrange
        Owner owner = new Owner();
        owner.setId(1L);
        owner.setBirthdate(LocalDate.now().minusYears(25));
        owner.setPassword("password");
        owner.setEmail("aa@aa.com");

        when(ownerRepository.findById(1L)).thenReturn(Optional.empty());
        when(ownerEntityMapper.toOwnerEntity(owner)).thenReturn(new OwnerEntity());

        // Act & Assert
        assertDoesNotThrow(() -> ownerAdapter.saveOwner(owner));
        verify(ownerRepository, times(1)).findById(1L);
        verify(ownerEntityMapper, times(1)).toOwnerEntity(owner);
        verify(ownerRepository, times(1)).save(any(OwnerEntity.class));
    }

    @Test
    void saveOwner_AlreadyExists() {
        // Arrange
        Owner owner = new Owner();
        owner.setId(1L);

        when(ownerRepository.findById(1L)).thenReturn(Optional.of(new OwnerEntity()));

        // Act & Assert
        assertThrows(OwnerAlreadyExistException.class, () -> ownerAdapter.saveOwner(owner));
        verify(ownerRepository, times(1)).findById(1L);
        verifyNoMoreInteractions(ownerEntityMapper);
        verifyNoMoreInteractions(ownerRepository);
    }

    @Test
    void saveOwner_UnderageOwner() {
        // Arrange
        Owner owner = new Owner();
        owner.setId(2L);
        owner.setBirthdate(LocalDate.now().minusYears(17));

        when(ownerRepository.findById(2L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(OwnerMustBeAdultException.class, () -> ownerAdapter.saveOwner(owner));
        verify(ownerRepository, times(1)).findById(2L);
        verifyNoMoreInteractions(ownerEntityMapper);
        verifyNoMoreInteractions(ownerRepository);
    }

    @Test
    void saveOwner_InvalidEmailFormat() {
        // Arrange
        Owner owner = new Owner();
        owner.setId(1L);
        owner.setEmail("invalid-email-format"); // Email inválido
        owner.setBirthdate(LocalDate.now().minusYears(25));
        owner.setPassword("password");

        when(ownerRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        InvalidEmailException exception = assertThrows(InvalidEmailException.class, () -> ownerAdapter.saveOwner(owner));
        assertEquals("Invalid email format", exception.getMessage());
        verify(ownerRepository, times(1)).findById(1L);
        verifyNoMoreInteractions(ownerEntityMapper);
        verifyNoMoreInteractions(ownerRepository);
    }
}