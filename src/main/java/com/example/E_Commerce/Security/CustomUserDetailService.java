package com.example.E_Commerce.Security;

import com.example.E_Commerce.Entity.User;
import com.example.E_Commerce.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetails loadUserByUsername(String username) {
        User user =  userRepository.findByUsername(username).orElseThrow(()->new UsernameNotFoundException("User not Found!!"));
        return new CustomUserDetails(user);
    }
}
