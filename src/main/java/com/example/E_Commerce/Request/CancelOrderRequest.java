package com.example.E_Commerce.Request;

import com.example.E_Commerce.DTO.Enums.CancelReason;
import lombok.Data;

@Data
public class CancelOrderRequest {

    private CancelReason cancelReason;

}
