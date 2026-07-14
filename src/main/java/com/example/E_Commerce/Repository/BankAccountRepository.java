package com.example.E_Commerce.Repository;

import com.example.E_Commerce.Entity.BankAccount;
import com.example.E_Commerce.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BankAccountRepository extends JpaRepository<BankAccount, Long> {

    List<BankAccount> findByUser(User user);

    Optional<BankAccount> findByUserAndPrimaryAccountTrue(User user);

    Optional<BankAccount> findByAccountIdAndUser(Long paymentMethodId, User user);
}