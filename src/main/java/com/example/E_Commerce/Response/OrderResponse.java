package com.example.E_Commerce.Response;

import com.example.E_Commerce.DTO.Enums.CancelReason;
import com.example.E_Commerce.DTO.OrderItemResponseDto;
import com.example.E_Commerce.DTO.Enums.StatusType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponse {

    private Long Id;

    private double TotalAmount;

    private StatusType Status;

    private LocalDate OrderDate;

    private LocalDateTime createdAt;

    private  LocalDateTime updatedAt;

    private CancelReason cancelReason;

    private List<OrderItemResponseDto> items;
}
