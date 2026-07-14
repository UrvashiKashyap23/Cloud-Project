package com.example.E_Commerce.Response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserBankAccountResponse {

    private String name;
    private String phoneNumber;
    private List<BankAccountResponse> bankAccounts;
}
