package com.example.E_Commerce.Response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BankAccountResponse {

    private Long accountId;

    private String accountHolderName;

    private String bankName;

    private String accountNumber;

    private String ifscCode;

    private boolean primaryAccount;
}