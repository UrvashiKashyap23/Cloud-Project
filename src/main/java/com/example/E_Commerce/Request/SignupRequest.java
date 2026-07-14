package com.example.E_Commerce.Request;

import com.example.E_Commerce.DTO.Enums.Gender;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SignupRequest{

    private String username;

    private String fullName;

    private String email;

    private String phoneNumber;

    private String password;

    private Gender gender;
}