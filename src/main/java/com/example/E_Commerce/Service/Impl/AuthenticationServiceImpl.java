package com.example.E_Commerce.Service.Impl;

import com.example.E_Commerce.DTO.Enums.Role;
import com.example.E_Commerce.Entity.User;
import com.example.E_Commerce.Repository.UserRepository;
import com.example.E_Commerce.Request.*;
import com.example.E_Commerce.Response.AuthResponse;
import com.example.E_Commerce.Response.BaseApiResponse;
import com.example.E_Commerce.Response.UserProfile;
import com.example.E_Commerce.Security.CustomUserDetails;
import com.example.E_Commerce.Security.JwtService;
import com.example.E_Commerce.Service.AuthenticationService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;


    @Override
    public BaseApiResponse<AuthResponse> signUp(SignupRequest signupRequest) {

        log.info("Registering new user with username: {}", signupRequest.getUsername());

        try {

            if (userRepository.existsByUsername(signupRequest.getUsername())) {
                throw new RuntimeException("Username already exists.");
            }

            if (userRepository.existsByEmail(signupRequest.getEmail())) {
                throw new RuntimeException("Email already exists.");
            }

            if (userRepository.existsByPhoneNumber(signupRequest.getPhoneNumber())) {
                throw new RuntimeException("Phone number already exists.");
            }

            User user = modelMapper.map(signupRequest, User.class);

            user.setPassword(passwordEncoder.encode(signupRequest.getPassword()));

            user.setRole(Role.USER);

            User savedUser = userRepository.save(user);

            String token = jwtService.generateToken(new CustomUserDetails(savedUser));

            AuthResponse authResponse = new AuthResponse();

            authResponse.setToken(token);
            authResponse.setUserId(savedUser.getUserId());
            authResponse.setName(savedUser.getFullName());
            authResponse.setRole(savedUser.getRole().name());

            return BaseApiResponse.<AuthResponse>builder()
                    .code(200)
                    .message("User registered successfully.")
                    .data(authResponse)
                    .build();

        } catch (Exception e) {

            log.error("Error while registering user: {}", e.getMessage());

            return BaseApiResponse.<AuthResponse>builder()
                    .code(500)
                    .message(e.getMessage())
                    .data(null)
                    .build();
        }
    }
    @Override
    public BaseApiResponse<AuthResponse> login(LoginRequest loginRequest) {

        log.info("User login request for username: {}", loginRequest.getUsername());

        try {

            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(),
                            loginRequest.getPassword()
                    )
            );

            User user = userRepository.findByUsername(loginRequest.getUsername())
                    .orElseThrow(() ->
                            new RuntimeException("User not found."));

            String token = jwtService.generateToken(new CustomUserDetails(user));

            AuthResponse authResponse = new AuthResponse();

            authResponse.setToken(token);
            authResponse.setUserId(user.getUserId());
            authResponse.setName(user.getFullName());
            authResponse.setRole(user.getRole().name());

            return BaseApiResponse.<AuthResponse>builder()
                    .code(200)
                    .message("Login successful.")
                    .data(authResponse)
                    .build();

        } catch (Exception e) {

            log.error("Error while logging in: {}", e.getMessage());

            return BaseApiResponse.<AuthResponse>builder()
                    .code(500)
                    .message(e.getMessage())
                    .data(null)
                    .build();
        }
    }

    @Override
    public BaseApiResponse<UserProfile> getUserProfile(Long userId) {

        log.info("Fetching profile for user: {}", userId);

        try {

            User user = userRepository.findById(userId)
                    .orElseThrow(() ->
                            new RuntimeException("User not found."));

            UserProfile response = modelMapper.map(user, UserProfile.class);

            return BaseApiResponse.<UserProfile>builder()
                    .code(200)
                    .message("Profile fetched successfully.")
                    .data(response)
                    .build();

        } catch (Exception e) {

            log.error("Error while fetching profile: {}", e.getMessage());

            return BaseApiResponse.<UserProfile>builder()
                    .code(500)
                    .message(e.getMessage())
                    .data(null)
                    .build();
        }
    }

    @Override
    @Transactional
    public BaseApiResponse<UserProfile> updateUserProfile(
            Long userId,
            UpdateProfileRequest request) {

        log.info("Updating profile for user: {}", userId);

        try {

            User user = userRepository.findById(userId)
                    .orElseThrow(() ->
                            new RuntimeException("User not found."));

            if (request.getName() != null) {
                user.setFullName(request.getName());
            }

            if (request.getUsername() != null) {

                if (userRepository.existsByUsername(request.getUsername())
                        && !user.getUsername().equals(request.getUsername())) {

                    throw new RuntimeException("Username already exists.");
                }

                user.setUsername(request.getUsername());
            }

            User updatedUser = userRepository.save(user);

            UserProfile response = modelMapper.map(updatedUser, UserProfile.class);

            return BaseApiResponse.<UserProfile>builder()
                    .code(200)
                    .message("Profile updated successfully.")
                    .data(response)
                    .build();

        } catch (Exception e) {

            log.error("Error while updating profile: {}", e.getMessage());

            return BaseApiResponse.<UserProfile>builder()
                    .code(500)
                    .message(e.getMessage())
                    .data(null)
                    .build();
        }
    }

    @Override
    @Transactional
    public BaseApiResponse<String> changePassword(Long userId, ChangePasswordRequest request) {

        log.info("Changing password for user: {}", userId);

        try {

            User user = userRepository.findById(userId)
                    .orElseThrow(() ->
                            new RuntimeException("User not found."));

            if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
                throw new RuntimeException("Old password is incorrect.");
            }

            user.setPassword(passwordEncoder.encode(request.getNewPassword()));

            userRepository.save(user);

            return BaseApiResponse.<String>builder()
                    .code(200)
                    .message("Password changed successfully.")
                    .data("Password changed successfully.")
                    .build();

        } catch (Exception e) {

            log.error("Error while changing password: {}", e.getMessage());

            return BaseApiResponse.<String>builder()
                    .code(500)
                    .message(e.getMessage())
                    .data(null)
                    .build();
        }
    }

    @Override
    @Transactional
    public BaseApiResponse<String> updateEmail(Long userId, UpdateEmailRequest request) {

        log.info("Updating email for user: {}", userId);

        try {

            User user = userRepository.findById(userId)
                    .orElseThrow(() ->
                            new RuntimeException("User not found."));

            if (userRepository.existsByEmail(request.getEmail())
                    && !user.getEmail().equals(request.getEmail())) {
                throw new RuntimeException("Email already exists.");
            }

            user.setEmail(request.getEmail());

            userRepository.save(user);

            return BaseApiResponse.<String>builder()
                    .code(200)
                    .message("Email updated successfully.")
                    .data("Email updated successfully.")
                    .build();

        } catch (Exception e) {

            log.error("Error while updating email: {}", e.getMessage());

            return BaseApiResponse.<String>builder()
                    .code(500)
                    .message(e.getMessage())
                    .data(null)
                    .build();
        }
    }

    @Override
    @Transactional
    public BaseApiResponse<String> updatePhoneNumber(Long userId, UpdatePhoneNumberRequest request) {

        log.info("Updating phone number for user: {}", userId);

        try {

            User user = userRepository.findById(userId)
                    .orElseThrow(() ->
                            new RuntimeException("User not found."));

            if (userRepository.existsByPhoneNumber(request.getPhoneNumber())
                    && !user.getPhoneNumber().equals(request.getPhoneNumber())) {
                throw new RuntimeException("Phone number already exists.");
            }

            user.setPhoneNumber(request.getPhoneNumber());

            userRepository.save(user);

            return BaseApiResponse.<String>builder()
                    .code(200)
                    .message("Phone number updated successfully.")
                    .data("Phone number updated successfully.")
                    .build();

        } catch (Exception e) {

            log.error("Error while updating phone number: {}", e.getMessage());

            return BaseApiResponse.<String>builder()
                    .code(500)
                    .message(e.getMessage())
                    .data(null)
                    .build();
        }
    }

    @Override
    @Transactional
    public BaseApiResponse<String> deleteUser(Long userId) {

        log.info("Deleting user with id: {}", userId);

        try {

            User user = userRepository.findById(userId)
                    .orElseThrow(() ->
                            new RuntimeException("User not found."));

            userRepository.delete(user);

            return BaseApiResponse.<String>builder()
                    .code(200)
                    .message("User deleted successfully.")
                    .data("User deleted successfully.")
                    .build();

        } catch (Exception e) {

            log.error("Error while deleting user: {}", e.getMessage());

            return BaseApiResponse.<String>builder()
                    .code(500)
                    .message(e.getMessage())
                    .data(null)
                    .build();
        }
    }
}
