package com.example.E_Commerce.Response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddressResponse {

    private Long addressId;

    private String fullName;

    private String phoneNumber;

    private String houseNo;

    private String area;

    private String city;

    private String state;

    private String pincode;

    private String country;

    private String landmark;

    private boolean primaryAddress;
}