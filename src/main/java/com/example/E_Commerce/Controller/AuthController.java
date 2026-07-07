package com.example.E_Commerce.Controller;

import com.example.E_Commerce.DTO.*;
import com.example.E_Commerce.Service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/auth")
public class AuthController {

    private final AuthenticationService authenticationService;

    @PostMapping("/signup")
    public AuthResponseDto SignUp(@RequestBody SignupRequestDto signupRequestDto){
        return authenticationService.signUp(signupRequestDto);
    }

    @PostMapping("/login")
    public AuthResponseDto Login(@RequestBody LoginRequestDto loginRequestDto){
        return  authenticationService.login(loginRequestDto);
    }

    @GetMapping("/get/profile/{userId}")
    public UserProfileDto getUserProfile(@PathVariable Long userId) {
        return authenticationService.getUserProfile(userId);
    }

    @PutMapping("/update/profile/{userId}")
    public UserProfileDto updateUserProfile(@PathVariable Long userId, @RequestBody UpdateProfileRequestDto requestDto) {
        return authenticationService.updateUserProfile(userId, requestDto);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout() {
        SecurityContextHolder.clearContext();
        return ResponseEntity.ok("Logged out successfully.");
    }
}
