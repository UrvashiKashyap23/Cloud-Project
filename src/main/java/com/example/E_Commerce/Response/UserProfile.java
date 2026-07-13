package com.example.E_Commerce.Response;

import lombok.Data;

@Data
public class UserProfile {
    private Long userId;

    private String name;

    private String username;

    private String role;
}