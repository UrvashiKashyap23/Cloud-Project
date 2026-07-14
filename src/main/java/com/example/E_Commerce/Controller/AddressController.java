package com.example.E_Commerce.Controller;

import com.example.E_Commerce.Request.AddressRequest;
import com.example.E_Commerce.Response.AddressResponse;
import com.example.E_Commerce.Response.BaseApiResponse;
import com.example.E_Commerce.Response.UserAddressResponse;
import com.example.E_Commerce.Service.AddressService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/address")
@RequiredArgsConstructor
public class AddressController {

    private final AddressService addressService;

    @PostMapping
    public ResponseEntity<BaseApiResponse<AddressResponse>> addAddress(@Valid @RequestBody AddressRequest request) {

        return ResponseEntity.ok(addressService.addAddress(request));
    }

    @GetMapping
    public ResponseEntity<BaseApiResponse<UserAddressResponse>>getAllAddresses() {

        return ResponseEntity.ok(addressService.getAllAddresses());
    }

    @GetMapping("/{addressId}")
    public ResponseEntity<BaseApiResponse<AddressResponse>> getAddressById(@PathVariable Long addressId) {

        return ResponseEntity.ok(addressService.getAddressById(addressId));
    }

    @PutMapping("/{addressId}")
    public ResponseEntity<BaseApiResponse<AddressResponse>> updateAddress(@PathVariable Long addressId, @Valid @RequestBody AddressRequest request) {

        return ResponseEntity.ok(addressService.updateAddress(addressId, request));
    }

    @DeleteMapping("/{addressId}")
    public ResponseEntity<BaseApiResponse<String>> deleteAddress(@PathVariable Long addressId) {

        return ResponseEntity.ok(addressService.deleteAddress(addressId));
    }

    @PutMapping("/{addressId}/primary")
    public ResponseEntity<BaseApiResponse<String>> makePrimaryAddress(@PathVariable Long addressId) {

        return ResponseEntity.ok(addressService.makePrimaryAddress(addressId));
    }

    @GetMapping("/primary")
    public ResponseEntity<BaseApiResponse<AddressResponse>> getPrimaryAddress() {

        return ResponseEntity.ok(addressService.getPrimaryAddress());
    }
}