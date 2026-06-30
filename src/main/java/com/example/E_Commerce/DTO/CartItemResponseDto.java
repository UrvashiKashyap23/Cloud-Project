package com.example.E_Commerce.DTO;

import com.example.E_Commerce.Entity.Cart;
import com.example.E_Commerce.Entity.Product;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CartItemResponseDto {


    private Long CartItemId;

    private Long productId;

    private String productName;

    private Double price;

    private int quantity;

    private Double totalPrice;



}
