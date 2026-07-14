package com.example.E_Commerce.Service.Impl;

import com.example.E_Commerce.Entity.BankAccount;
import com.example.E_Commerce.Entity.User;
import com.example.E_Commerce.Repository.BankAccountRepository;
import com.example.E_Commerce.Repository.UserRepository;
import com.example.E_Commerce.Request.BankAccountRequest;
import com.example.E_Commerce.Response.BaseApiResponse;
import com.example.E_Commerce.Response.BankAccountResponse;
import com.example.E_Commerce.Response.UserBankAccountResponse;
import com.example.E_Commerce.Service.BankAccountService;
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
public class BankAccountServiceImpl implements BankAccountService {

    private final BankAccountRepository bankAccountRepository;
    private final UserRepository userRepository;

    private User getAuthenticatedUser() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String username = authentication.getName();

        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found."));
    }

    private BankAccountResponse mapToResponse(BankAccount bankAccount) {

        String accountNumber = bankAccount.getAccountNumber();

        String maskedAccountNumber = "XXXXXX" + accountNumber.substring(accountNumber.length() - 4);

        return BankAccountResponse.builder()
                .accountId(bankAccount.getAccountId())
                .accountHolderName(bankAccount.getAccountHolderName())
                .bankName(bankAccount.getBankName())
                .accountNumber(maskedAccountNumber)
                .ifscCode(bankAccount.getIfscCode())
                .primaryAccount(bankAccount.isPrimaryAccount())
                .build();
    }

    @Override
    public BaseApiResponse<BankAccountResponse> addBankAccount(BankAccountRequest request) {

        log.info("Adding new payment method for authenticated user.");

        try {

            User user = getAuthenticatedUser();

            List<BankAccount> bankAccounts = bankAccountRepository.findByUser(user);

            if (!bankAccounts.isEmpty()) {

                for (BankAccount bankAccount : bankAccounts) {
                    bankAccount.setPrimaryAccount(false);
                }

                bankAccountRepository.saveAll(bankAccounts);
            }

            BankAccount bankAccount = BankAccount.builder()
                    .user(user)
                    .accountHolderName(request.getAccountHolderName())
                    .bankName(request.getBankName())
                    .accountNumber(request.getAccountNumber())
                    .ifscCode(request.getIfscCode())
                    .primaryAccount(true)
                    .build();

            BankAccount savedBankAccount =
                    bankAccountRepository.save(bankAccount);

            log.info("Payment method added successfully with id {}", savedBankAccount.getAccountId());

            return BaseApiResponse.<BankAccountResponse>builder()
                    .code(HttpStatus.OK.value())
                    .message("Payment method added successfully.")
                    .data(mapToResponse(savedBankAccount))
                    .build();

        } catch (Exception e) {

            log.error("Error adding payment method: {}", e.getMessage());

            return BaseApiResponse.<BankAccountResponse>builder()
                    .code(HttpStatus.BAD_REQUEST.value())
                    .message("Failed to add payment method: " + e.getMessage())
                    .data(null)
                    .build();
        }
    }

    @Override
    public BaseApiResponse<UserBankAccountResponse> getAllBankAccounts() {

        log.info("Fetching all payment methods.");

        try {

            User user = getAuthenticatedUser();

            List<BankAccountResponse> bankAccounts = bankAccountRepository
                    .findByUser(user)
                    .stream()
                    .map(this::mapToResponse)
                    .toList();

            UserBankAccountResponse response = UserBankAccountResponse.builder()
                    .name(user.getFullName())
                    .phoneNumber(user.getPhoneNumber())
                    .bankAccounts(bankAccounts)
                    .build();

            return BaseApiResponse.<UserBankAccountResponse>builder()
                    .code(HttpStatus.OK.value())
                    .message("Bank accounts fetched successfully.")
                    .data(response)
                    .build();

        } catch (Exception e) {

            log.error("Error fetching payment methods: {}", e.getMessage());

            return BaseApiResponse.<UserBankAccountResponse>builder()
                    .code(HttpStatus.BAD_REQUEST.value())
                    .message(e.getMessage())
                    .data(null)
                    .build();
        }
    }

    @Override
    public BaseApiResponse<BankAccountResponse> getBankAccountById(Long accountId) {

        log.info("Fetching payment method with id {}", accountId);

        try {

            User user = getAuthenticatedUser();

            BankAccount bankAccount = bankAccountRepository
                    .findByAccountIdAndUser(accountId, user)
                    .orElseThrow(() -> new RuntimeException("Payment method not found."));

            return BaseApiResponse.<BankAccountResponse>builder()
                    .code(HttpStatus.OK.value())
                    .message("Payment method fetched successfully.")
                    .data(mapToResponse(bankAccount))
                    .build();

        } catch (Exception e) {

            log.error("Error fetching payment method: {}", e.getMessage());

            return BaseApiResponse.<BankAccountResponse>builder()
                    .code(HttpStatus.BAD_REQUEST.value())
                    .message(e.getMessage())
                    .data(null)
                    .build();
        }
    }

    @Override
    public BaseApiResponse<BankAccountResponse> updateBankAccount(Long paymentMethodId, BankAccountRequest request) {

        log.info("Updating payment method {}", paymentMethodId);

        try {

            User user = getAuthenticatedUser();

            BankAccount bankAccount = bankAccountRepository
                    .findByAccountIdAndUser(paymentMethodId, user)
                    .orElseThrow(() -> new RuntimeException("Payment method not found."));

            bankAccount.setAccountHolderName(request.getAccountHolderName());
            bankAccount.setBankName(request.getBankName());
            bankAccount.setAccountNumber(request.getAccountNumber());
            bankAccount.setIfscCode(request.getIfscCode());

            BankAccount updatedBankAccount =
                    bankAccountRepository.save(bankAccount);

            return BaseApiResponse.<BankAccountResponse>builder()
                    .code(HttpStatus.OK.value())
                    .message("Payment method updated successfully.")
                    .data(mapToResponse(updatedBankAccount))
                    .build();

        } catch (Exception e) {

            log.error("Error updating payment method: {}", e.getMessage());

            return BaseApiResponse.<BankAccountResponse>builder()
                    .code(HttpStatus.BAD_REQUEST.value())
                    .message(e.getMessage())
                    .data(null)
                    .build();
        }
    }

    @Override
    public BaseApiResponse<String> deleteBankAccount(Long paymentMethodId) {

        log.info("Deleting payment method {}", paymentMethodId);

        try {

            User user = getAuthenticatedUser();

            BankAccount bankAccount = bankAccountRepository
                    .findByAccountIdAndUser(paymentMethodId, user)
                    .orElseThrow(() -> new RuntimeException("Payment method not found."));

            boolean wasPrimary = bankAccount.isPrimaryAccount();

            bankAccountRepository.delete(bankAccount);

            if (wasPrimary) {

                List<BankAccount> remainingBankAccounts =
                        bankAccountRepository.findByUser(user);

                if (!remainingBankAccounts.isEmpty()) {

                    BankAccount newPrimary = remainingBankAccounts.get(0);

                    newPrimary.setPrimaryAccount(true);

                    bankAccountRepository.save(newPrimary);
                }
            }

            return BaseApiResponse.<String>builder()
                    .code(HttpStatus.OK.value())
                    .message("Payment method deleted successfully.")
                    .data(null)
                    .build();

        } catch (Exception e) {

            log.error("Error deleting payment method: {}", e.getMessage());

            return BaseApiResponse.<String>builder()
                    .code(HttpStatus.BAD_REQUEST.value())
                    .message(e.getMessage())
                    .data(null)
                    .build();
        }
    }

    @Override
    public BaseApiResponse<String> makePrimaryBankAccount(Long paymentMethodId) {

        log.info("Changing primary payment method to {}", paymentMethodId);

        try {

            User user = getAuthenticatedUser();

            List<BankAccount> bankAccounts =
                    bankAccountRepository.findByUser(user);

            BankAccount selectedBankAccount = null;

            for (BankAccount bankAccount : bankAccounts) {

                if (bankAccount.getAccountId().equals(paymentMethodId)) {
                    selectedBankAccount = bankAccount;
                }

                bankAccount.setPrimaryAccount(false);
            }

            if (selectedBankAccount == null) {
                throw new RuntimeException("Payment method not found.");
            }

            selectedBankAccount.setPrimaryAccount(true);

            bankAccountRepository.saveAll(bankAccounts);

            return BaseApiResponse.<String>builder()
                    .code(HttpStatus.OK.value())
                    .message("Primary payment method updated successfully.")
                    .data(null)
                    .build();

        } catch (Exception e) {

            log.error("Error changing primary payment method: {}", e.getMessage());

            return BaseApiResponse.<String>builder()
                    .code(HttpStatus.BAD_REQUEST.value())
                    .message(e.getMessage())
                    .data(null)
                    .build();
        }
    }

    @Override
    public BaseApiResponse<BankAccountResponse> getPrimaryBankAccount() {

        log.info("Fetching primary payment method.");

        try {

            User user = getAuthenticatedUser();

            BankAccount bankAccount = bankAccountRepository
                    .findByUserAndPrimaryAccountTrue(user)
                    .orElseThrow(() -> new RuntimeException("Primary payment method not found."));

            return BaseApiResponse.<BankAccountResponse>builder()
                    .code(HttpStatus.OK.value())
                    .message("Primary payment method fetched successfully.")
                    .data(mapToResponse(bankAccount))
                    .build();

        } catch (Exception e) {

            log.error("Error fetching primary payment method: {}", e.getMessage());

            return BaseApiResponse.<BankAccountResponse>builder()
                    .code(HttpStatus.BAD_REQUEST.value())
                    .message(e.getMessage())
                    .data(null)
                    .build();
        }
    }

}
