package com.example.E_Commerce.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartItemRequestDto {

    private Long productId;

    private int quantity;
}
