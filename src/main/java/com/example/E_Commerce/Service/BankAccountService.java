package com.example.E_Commerce.Service;

import com.example.E_Commerce.Request.BankAccountRequest;
import com.example.E_Commerce.Response.BankAccountResponse;
import com.example.E_Commerce.Response.BaseApiResponse;
import com.example.E_Commerce.Response.UserBankAccountResponse;

import java.util.List;

public interface BankAccountService {

    BaseApiResponse<BankAccountResponse> addBankAccount(BankAccountRequest request);

    BaseApiResponse<UserBankAccountResponse> getAllBankAccounts();

    BaseApiResponse<BankAccountResponse> getBankAccountById(Long accountId);

    BaseApiResponse<BankAccountResponse> updateBankAccount(Long accountId, BankAccountRequest request);

    BaseApiResponse<String> deleteBankAccount(Long accountId);

    BaseApiResponse<String> makePrimaryBankAccount(Long accountId);

    BaseApiResponse<BankAccountResponse> getPrimaryBankAccount();
}