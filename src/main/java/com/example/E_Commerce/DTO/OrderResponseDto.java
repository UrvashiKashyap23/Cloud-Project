package com.example.E_Commerce.DTO;

import com.example.E_Commerce.DTO.type.StatusType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
public class OrderResponseDto {

    private Long Id;

    private double TotalAmount;

    private StatusType Status;

    private LocalDate OrderDate;

    private LocalDateTime createdAt;

    private  LocalDateTime updatedAt;

    private List<OrderItemResponseDto> items;
}
