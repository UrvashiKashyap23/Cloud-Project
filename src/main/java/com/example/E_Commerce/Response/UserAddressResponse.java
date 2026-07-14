package com.example.E_Commerce.Response;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserAddressResponse {

    private String name;
    private String phoneNumber;
    private List<AddressResponse> addresses;
}