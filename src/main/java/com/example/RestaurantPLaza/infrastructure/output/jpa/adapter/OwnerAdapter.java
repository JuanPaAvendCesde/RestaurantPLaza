package com.example.RestaurantPLaza.infrastructure.output.jpa.adapter;

import com.example.RestaurantPLaza.domain.model.Owner;
import com.example.RestaurantPLaza.domain.spi.IOwnerPersistencePort;
import com.example.RestaurantPLaza.infrastructure.exception.OwnerAlreadyExistException;
import com.example.RestaurantPLaza.infrastructure.exception.OwnerMustBeAdultException;
import com.example.RestaurantPLaza.infrastructure.output.jpa.entity.OwnerEntity;
import com.example.RestaurantPLaza.infrastructure.output.jpa.mapper.OwnerEntityMapper;
import com.example.RestaurantPLaza.infrastructure.output.jpa.repository.IOwnerRepository;
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
        String passEncrypted = passWithBcrypt(owner.getPassword());
        owner.setPassword(passEncrypted);
        ownerRepository.save(ownerEntityMapper.toOwnerEntity(owner));
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
