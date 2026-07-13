package com.example.E_Commerce.Controller;

import com.example.E_Commerce.Request.*;
import com.example.E_Commerce.Response.AuthResponse;
import com.example.E_Commerce.Response.BaseApiResponse;
import com.example.E_Commerce.Response.UserProfile;
import com.example.E_Commerce.Service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/auth")
public class AuthController {

    private final AuthenticationService authenticationService;

    @PostMapping("/signup")
    public BaseApiResponse<AuthResponse> signUp(@RequestBody SignupRequest signupRequest){

        return authenticationService.signUp(signupRequest);
    }

    @PostMapping("/login")
    public BaseApiResponse<AuthResponse> login(@RequestBody LoginRequest loginRequest){

        return authenticationService.login(loginRequest);
    }

    @GetMapping("/get/profile/{userId}")
    public BaseApiResponse<UserProfile> getUserProfile(@PathVariable Long userId){

        return authenticationService.getUserProfile(userId);
    }

    @PutMapping("/update/profile/{userId}")
    public BaseApiResponse<UserProfile> updateUserProfile(@PathVariable Long userId, @RequestBody UpdateProfileRequest request){

        return authenticationService.updateUserProfile(userId, request);
    }

    @PutMapping("/change/password/{userId}")
    public BaseApiResponse<String> changePassword(@PathVariable Long userId, @RequestBody ChangePasswordRequest request){

        return authenticationService.changePassword(userId, request);
    }

    @PutMapping("/update/email/{userId}")
    public BaseApiResponse<String> updateEmail(@PathVariable Long userId, @RequestBody UpdateEmailRequest request){

        return authenticationService.updateEmail(userId, request);
    }

    @PutMapping("/update/phone/{userId}")
    public BaseApiResponse<String> updatePhoneNumber(@PathVariable Long userId, @RequestBody UpdatePhoneNumberRequest request){

        return authenticationService.updatePhoneNumber(userId, request);
    }

    @DeleteMapping("/delete/{userId}")
    public BaseApiResponse<String> deleteUser(@PathVariable Long userId){

        return authenticationService.deleteUser(userId);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout() {

        SecurityContextHolder.clearContext();

        return ResponseEntity.ok("Logged out successfully.");
    }
}