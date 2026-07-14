package com.example.E_Commerce.Controller;

import com.example.E_Commerce.Request.BankAccountRequest;
import com.example.E_Commerce.Response.BankAccountResponse;
import com.example.E_Commerce.Response.BaseApiResponse;
import com.example.E_Commerce.Response.UserBankAccountResponse;
import com.example.E_Commerce.Service.BankAccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/bank-account")
@RequiredArgsConstructor
public class BankAccountController {

    private final BankAccountService bankAccountService;

    @PostMapping
    public ResponseEntity<BaseApiResponse<BankAccountResponse>> addBankAccount(
            @Valid @RequestBody BankAccountRequest request) {

        return ResponseEntity.ok(bankAccountService.addBankAccount(request));
    }

    @GetMapping
    public ResponseEntity<BaseApiResponse<UserBankAccountResponse>> getAllBankAccounts() {

        return ResponseEntity.ok(bankAccountService.getAllBankAccounts());
    }

    @GetMapping("/{accountId}")
    public ResponseEntity<BaseApiResponse<BankAccountResponse>> getBankAccountById(
            @PathVariable Long accountId) {

        return ResponseEntity.ok(bankAccountService.getBankAccountById(accountId));
    }

    @PutMapping("/{accountId}")
    public ResponseEntity<BaseApiResponse<BankAccountResponse>> updateBankAccount(
            @PathVariable Long accountId,
            @Valid @RequestBody BankAccountRequest request) {

        return ResponseEntity.ok(bankAccountService.updateBankAccount(accountId, request));
    }

    @DeleteMapping("/{accountId}")
    public ResponseEntity<BaseApiResponse<String>> deleteBankAccount(
            @PathVariable Long accountId) {

        return ResponseEntity.ok(bankAccountService.deleteBankAccount(accountId));
    }

    @PutMapping("/{accountId}/primary")
    public ResponseEntity<BaseApiResponse<String>> makePrimaryBankAccount(
            @PathVariable Long accountId) {

        return ResponseEntity.ok(bankAccountService.makePrimaryBankAccount(accountId));
    }

    @GetMapping("/primary")
    public ResponseEntity<BaseApiResponse<BankAccountResponse>> getPrimaryBankAccount() {

        return ResponseEntity.ok(bankAccountService.getPrimaryBankAccount());
    }
}