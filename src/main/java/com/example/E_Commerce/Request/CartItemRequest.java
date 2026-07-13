package com.example.E_Commerce.Request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartItemRequest {

    private Long productId;

    private int quantity;
}
