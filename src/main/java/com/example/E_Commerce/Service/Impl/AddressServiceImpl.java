package com.example.E_Commerce.Service.Impl;

import com.example.E_Commerce.Entity.Address;
import com.example.E_Commerce.Entity.User;
import com.example.E_Commerce.Repository.AddressRepository;
import com.example.E_Commerce.Repository.UserRepository;
import com.example.E_Commerce.Request.AddressRequest;
import com.example.E_Commerce.Response.AddressResponse;
import com.example.E_Commerce.Response.BaseApiResponse;
import com.example.E_Commerce.Service.AddressService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;
    private final UserRepository userRepository;

    private User getAuthenticatedUser() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String username = authentication.getName();

        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found."));
    }

    private AddressResponse mapToResponse(Address address) {

        return AddressResponse.builder()
                .addressId(address.getAddressId())
                .fullName(address.getFullName())
                .phoneNumber(address.getPhoneNumber())
                .houseNo(address.getHouseNo())
                .area(address.getArea())
                .city(address.getCity())
                .state(address.getState())
                .pincode(address.getPincode())
                .country(address.getCountry())
                .landmark(address.getLandmark())
                .primaryAddress(address.isPrimaryAddress())
                .build();
    }

    @Override
    public BaseApiResponse<AddressResponse> addAddress(AddressRequest request) {

        log.info("Adding new address for authenticated user.");

        try {

            User user = getAuthenticatedUser();

            List<Address> existingAddresses = addressRepository.findByUser(user);

            if (!existingAddresses.isEmpty()) {

                for (Address address : existingAddresses) {
                    address.setPrimaryAddress(false);
                }

                addressRepository.saveAll(existingAddresses);
            }

            Address address = Address.builder()
                    .user(user)
                    .fullName(request.getFullName())
                    .phoneNumber(request.getPhoneNumber())
                    .houseNo(request.getHouseNo())
                    .area(request.getArea())
                    .city(request.getCity())
                    .state(request.getState())
                    .pincode(request.getPincode())
                    .country(request.getCountry())
                    .landmark(request.getLandmark())
                    .primaryAddress(true)
                    .build();

            Address savedAddress = addressRepository.save(address);

            log.info("Address added successfully with id {}",
                    savedAddress.getAddressId());

            return BaseApiResponse.<AddressResponse>builder()
                    .code(HttpStatus.OK.value())
                    .message("Address added successfully.")
                    .data(mapToResponse(savedAddress))
                    .build();

        } catch (Exception e) {

            log.error("Error while adding address: {}", e.getMessage());

            return BaseApiResponse.<AddressResponse>builder()
                    .code(HttpStatus.BAD_REQUEST.value())
                    .message("Failed to add address: " + e.getMessage())
                    .data(null)
                    .build();
        }
    }

    @Override
    public BaseApiResponse<List<AddressResponse>> getAllAddresses() {

        log.info("Fetching all addresses of authenticated user.");

        try {

            User user = getAuthenticatedUser();

            List<AddressResponse> addressResponses = addressRepository
                    .findByUser(user)
                    .stream()
                    .map(this::mapToResponse)
                    .toList();

            return BaseApiResponse.<List<AddressResponse>>builder()
                    .code(HttpStatus.OK.value())
                    .message("Addresses fetched successfully.")
                    .data(addressResponses)
                    .build();

        } catch (Exception e) {

            log.error("Error fetching addresses: {}", e.getMessage());

            return BaseApiResponse.<List<AddressResponse>>builder()
                    .code(HttpStatus.BAD_REQUEST.value())
                    .message("Failed to fetch addresses: " + e.getMessage())
                    .data(null)
                    .build();
        }
    }

    @Override
    public BaseApiResponse<AddressResponse> getAddressById(Long addressId) {

        log.info("Fetching address with id {}", addressId);

        try {

            User user = getAuthenticatedUser();

            Address address = addressRepository.findById(addressId)
                    .orElseThrow(() ->
                            new RuntimeException("Address not found."));

            if (!address.getUser().getUserId().equals(user.getUserId())) {
                throw new RuntimeException("Unauthorized access to address.");
            }

            return BaseApiResponse.<AddressResponse>builder()
                    .code(HttpStatus.OK.value())
                    .message("Address fetched successfully.")
                    .data(mapToResponse(address))
                    .build();

        } catch (Exception e) {

            log.error("Error fetching address: {}", e.getMessage());

            return BaseApiResponse.<AddressResponse>builder()
                    .code(HttpStatus.BAD_REQUEST.value())
                    .message(e.getMessage())
                    .data(null)
                    .build();
        }
    }

    @Override
    public BaseApiResponse<AddressResponse> updateAddress(
            Long addressId,
            AddressRequest request) {

        log.info("Updating address {}", addressId);

        try {

            User user = getAuthenticatedUser();

            Address address = addressRepository.findById(addressId)
                    .orElseThrow(() ->
                            new RuntimeException("Address not found."));

            if (!address.getUser().getUserId().equals(user.getUserId())) {
                throw new RuntimeException("Unauthorized access.");
            }

            address.setFullName(request.getFullName());
            address.setPhoneNumber(request.getPhoneNumber());
            address.setHouseNo(request.getHouseNo());
            address.setArea(request.getArea());
            address.setCity(request.getCity());
            address.setState(request.getState());
            address.setPincode(request.getPincode());
            address.setCountry(request.getCountry());
            address.setLandmark(request.getLandmark());

            Address updatedAddress = addressRepository.save(address);

            return BaseApiResponse.<AddressResponse>builder()
                    .code(HttpStatus.OK.value())
                    .message("Address updated successfully.")
                    .data(mapToResponse(updatedAddress))
                    .build();

        } catch (Exception e) {

            log.error("Error updating address: {}", e.getMessage());

            return BaseApiResponse.<AddressResponse>builder()
                    .code(HttpStatus.BAD_REQUEST.value())
                    .message(e.getMessage())
                    .data(null)
                    .build();
        }
    }

    @Override
    public BaseApiResponse<String> deleteAddress(Long addressId) {

        log.info("Deleting address {}", addressId);

        try {

            User user = getAuthenticatedUser();

            Address address = addressRepository.findById(addressId)
                    .orElseThrow(() ->
                            new RuntimeException("Address not found."));

            if (!address.getUser().getUserId().equals(user.getUserId())) {
                throw new RuntimeException("Unauthorized access.");
            }

            boolean wasPrimary = address.isPrimaryAddress();

            addressRepository.delete(address);

            if (wasPrimary) {

                List<Address> remainingAddresses =
                        addressRepository.findByUser(user);

                if (!remainingAddresses.isEmpty()) {

                    Address newPrimary = remainingAddresses.getFirst();

                    newPrimary.setPrimaryAddress(true);

                    addressRepository.save(newPrimary);
                }
            }

            return BaseApiResponse.<String>builder()
                    .code(HttpStatus.OK.value())
                    .message("Address deleted successfully.")
                    .data(null)
                    .build();

        } catch (Exception e) {

            log.error("Error deleting address: {}", e.getMessage());

            return BaseApiResponse.<String>builder()
                    .code(HttpStatus.BAD_REQUEST.value())
                    .message(e.getMessage())
                    .data(null)
                    .build();
        }
    }

    @Override
    public BaseApiResponse<String> makePrimaryAddress(Long addressId) {

        log.info("Changing primary address to {}", addressId);

        try {

            User user = getAuthenticatedUser();

            List<Address> addresses = addressRepository.findByUser(user);

            Address selectedAddress = null;

            for (Address address : addresses) {

                if (address.getAddressId().equals(addressId)) {
                    selectedAddress = address;
                }

                address.setPrimaryAddress(false);
            }

            if (selectedAddress == null) {
                throw new RuntimeException("Address not found.");
            }

            selectedAddress.setPrimaryAddress(true);

            addressRepository.saveAll(addresses);

            return BaseApiResponse.<String>builder()
                    .code(HttpStatus.OK.value())
                    .message("Primary address updated successfully.")
                    .data(null)
                    .build();

        } catch (Exception e) {

            log.error("Error changing primary address: {}", e.getMessage());

            return BaseApiResponse.<String>builder()
                    .code(HttpStatus.BAD_REQUEST.value())
                    .message(e.getMessage())
                    .data(null)
                    .build();
        }
    }

    @Override
    public BaseApiResponse<AddressResponse> getPrimaryAddress() {

        log.info("Fetching primary address.");

        try {

            User user = getAuthenticatedUser();

            Address address = addressRepository
                    .findByUserAndPrimaryAddressTrue(user)
                    .orElseThrow(() ->
                            new RuntimeException("Primary address not found."));

            return BaseApiResponse.<AddressResponse>builder()
                    .code(HttpStatus.OK.value())
                    .message("Primary address fetched successfully.")
                    .data(mapToResponse(address))
                    .build();

        } catch (Exception e) {

            log.error("Error fetching primary address: {}", e.getMessage());

            return BaseApiResponse.<AddressResponse>builder()
                    .code(HttpStatus.BAD_REQUEST.value())
                    .message(e.getMessage())
                    .data(null)
                    .build();
        }
    }

}