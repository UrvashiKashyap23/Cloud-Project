package com.example.E_Commerce.Repository;

import com.example.E_Commerce.Entity.Address;
import com.example.E_Commerce.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AddressRepository extends JpaRepository<Address, Long> {

    List<Address> findByUser(User user);

    Optional<Address> findByUserAndPrimaryAddressTrue(User user);
}