package com.fest.backend.Service;

import com.fest.backend.CustomUserDetails;
import com.fest.backend.Entity.FestUser;
import com.fest.backend.Repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        FestUser user = userRepository.findByEmail(email);
        if(Objects.isNull(user))
        {
            System.out.println("User Not Found");
            throw new UsernameNotFoundException("User Not Found");
        }


        return new CustomUserDetails(user);
    }
}
