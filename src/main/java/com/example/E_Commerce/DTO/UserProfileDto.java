package com.example.E_Commerce.DTO;

import lombok.Data;

@Data
public class UserProfileDto {
    private Long userId;

    private String name;

    private String username;

    private String role;
}