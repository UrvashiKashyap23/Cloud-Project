package com.example.E_Commerce.Service.Impl;

import com.example.E_Commerce.DTO.*;
import com.example.E_Commerce.DTO.Enums.Role;
import com.example.E_Commerce.Entity.User;
import com.example.E_Commerce.Repository.UserRepository;
import com.example.E_Commerce.Security.CustomUserDetails;
import com.example.E_Commerce.Security.JwtService;
import com.example.E_Commerce.Service.AuthenticationService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;


    @Override
    public AuthResponseDto signUp(SignupRequestDto signupRequestDto) {
        if(userRepository.existsByUsername(signupRequestDto.getUsername())){
            throw new RuntimeException("Username already exists!!");
        }

        User user = modelMapper.map(signupRequestDto, User.class);
        user.setPassword(passwordEncoder.encode(signupRequestDto.getPassword()));
        user.setRole(Role.USER);
        User savedUser = userRepository.save(user);
        String token = jwtService.generateToken(new CustomUserDetails(user));

        AuthResponseDto authResponseDto = new AuthResponseDto();
        authResponseDto.setToken(token);
        authResponseDto.setUserId(savedUser.getUserId());
        authResponseDto.setName(savedUser.getName());
        authResponseDto.setRole(savedUser.getRole().name());

        return authResponseDto;
    }

    @Override
    public AuthResponseDto login(LoginRequestDto loginRequestDto){
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequestDto.getUsername(),
                        loginRequestDto.getPassword()
                )
        );

        User user = userRepository.findByUsername(loginRequestDto.getUsername()).orElseThrow(()->new RuntimeException("User not Found!!"));

        String token = jwtService.generateToken(new CustomUserDetails(user));

        AuthResponseDto authResponseDto =  new AuthResponseDto();

        authResponseDto.setToken(token);

        authResponseDto.setUserId(user.getUserId());

        authResponseDto.setName(user.getName());

        authResponseDto.setRole(user.getRole().name());

        return authResponseDto;
    }
    @Override
    public UserProfileDto getUserProfile(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found!"));

        return modelMapper.map(user, UserProfileDto.class);
    }

    @Override
    @Transactional
    public UserProfileDto updateUserProfile(Long userId, UpdateProfileRequestDto requestDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found!"));

        if (requestDto.getName() != null) {
            user.setName(requestDto.getName());
        }
        if (requestDto.getUsername() != null) {
            user.setUsername(requestDto.getUsername());
        }

        User updatedUser = userRepository.save(user);

        return modelMapper.map(updatedUser, UserProfileDto.class);
    }
}
