package com.example.E_Commerce.DTO;

import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemResponseDto {

    private Long productId;

    private String productName;

    private Integer quantity;

    private Double priceAtPurchase;

    private Double subtotal;
}
