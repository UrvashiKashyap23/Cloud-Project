package com.example.E_Commerce.Service;

import com.example.E_Commerce.DTO.*;


public interface AuthenticationService {

    AuthResponseDto signUp(SignupRequestDto signupRequestDto);

    AuthResponseDto login(LoginRequestDto loginRequestDto);

    UserProfileDto getUserProfile(Long userId);

    UserProfileDto updateUserProfile(Long userId, UpdateProfileRequestDto requestDto);

}
