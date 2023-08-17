package org.pragma.restaurantplaza.security;

import org.pragma.restaurantplaza.domain.model.User;
import org.pragma.restaurantplaza.infrastructure.output.jpa.repository.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailServiceImpl  implements UserDetailsService{

    @Autowired
    private IUserRepository userRepository;


    @Override
    public UserDetails loadUserByUsername(String email)throws UsernameNotFoundException {
       User user = userRepository
                .findOneByEmail(email)
                .orElseThrow(()->new UsernameNotFoundException("User not found"+ email));
        return new UserDetailsImpl(user);
    }


}
