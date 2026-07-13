package com.example.E_Commerce.Response;

import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartItemResponse {

    private Long cartItemId;

    private Long productId;

    private String productName;

    private Double productPrice;

    private int quantity;

    private Double totalPrice;
}
