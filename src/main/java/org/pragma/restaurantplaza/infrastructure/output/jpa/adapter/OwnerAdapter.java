package org.pragma.restaurantplaza.infrastructure.output.jpa.adapter;

import org.pragma.restaurantplaza.domain.model.Owner;
import org.pragma.restaurantplaza.domain.spi.IOwnerPersistencePort;
import org.pragma.restaurantplaza.infrastructure.exception.InvalidEmailException;
import org.pragma.restaurantplaza.infrastructure.exception.OwnerAlreadyExistException;
import org.pragma.restaurantplaza.infrastructure.exception.OwnerMustBeAdultException;
import org.pragma.restaurantplaza.infrastructure.output.jpa.entity.OwnerEntity;
import org.pragma.restaurantplaza.infrastructure.output.jpa.mapper.OwnerEntityMapper;
import org.pragma.restaurantplaza.infrastructure.output.jpa.repository.IOwnerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDate;
import java.util.List;
@RequiredArgsConstructor
public class OwnerAdapter implements IOwnerPersistencePort {

   private final IOwnerRepository ownerRepository;
   private final OwnerEntityMapper ownerEntityMapper;


    @Override
    public void saveOwner(Owner owner) {

        if(ownerRepository.findById(owner.getId()).isPresent() ) {
            throw new OwnerAlreadyExistException("Owner already exists");
        }
        LocalDate birthdate = owner.getBirthdate();
        LocalDate age = LocalDate.now().minusYears(18);
        if (birthdate.isAfter(age)) {
            throw new OwnerMustBeAdultException("Owner must be over 18 years old");
        }

        if (!isValidEmail(owner.getEmail())) {
            throw new InvalidEmailException("Invalid email format");
        }
        String passEncrypted = passWithBcrypt(owner.getPassword());
        owner.setPassword(passEncrypted);
        ownerRepository.save(ownerEntityMapper.toOwnerEntity(owner));
    }

    private boolean isValidEmail(String email) {
        String regex = "^(.+)@(.+)$";
        return email.matches(regex);
    }

    private String passWithBcrypt(String pass) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder.encode(pass);
    }


    @Override
    public List<Owner> getAllOwners() {
       List<OwnerEntity> ownerEntityList = ownerRepository.findAll();

       return ownerEntityMapper.toOwnerList(ownerEntityList);
    }
}
