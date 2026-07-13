package com.example.E_Commerce.Service;

import com.example.E_Commerce.Request.*;
import com.example.E_Commerce.Response.AuthResponse;
import com.example.E_Commerce.Response.BaseApiResponse;
import com.example.E_Commerce.Response.UserProfile;


public interface AuthenticationService {

    BaseApiResponse<AuthResponse> signUp(SignupRequest signupRequest);

    BaseApiResponse<AuthResponse> login(LoginRequest loginRequest);

    BaseApiResponse<UserProfile> getUserProfile(Long userId);

    BaseApiResponse<UserProfile> updateUserProfile(Long userId, UpdateProfileRequest request);

    BaseApiResponse<String> changePassword(Long userId, ChangePasswordRequest request);

    BaseApiResponse<String> updateEmail(Long userId, UpdateEmailRequest request);

    BaseApiResponse<String> updatePhoneNumber(Long userId, UpdatePhoneNumberRequest request);

    BaseApiResponse<String> deleteUser(Long userId);

}
