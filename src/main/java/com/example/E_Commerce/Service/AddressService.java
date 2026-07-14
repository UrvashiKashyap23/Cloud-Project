package com.example.E_Commerce.Service;

import com.example.E_Commerce.Request.AddressRequest;
import com.example.E_Commerce.Response.AddressResponse;
import com.example.E_Commerce.Response.BaseApiResponse;
import com.example.E_Commerce.Response.UserAddressResponse;

import java.util.List;

public interface AddressService {

    BaseApiResponse<AddressResponse> addAddress(AddressRequest request);

    BaseApiResponse<UserAddressResponse> getAllAddresses();

    BaseApiResponse<AddressResponse> getAddressById(Long addressId);

    BaseApiResponse<AddressResponse> updateAddress(Long addressId, AddressRequest request);

    BaseApiResponse<String> deleteAddress(Long addressId);

    BaseApiResponse<String> makePrimaryAddress(Long addressId);

    BaseApiResponse<AddressResponse> getPrimaryAddress();
}