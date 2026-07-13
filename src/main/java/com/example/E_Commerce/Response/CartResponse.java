package com.example.E_Commerce.Response;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartResponse {

    private Long cartId;

    private Double totalAmount;

    private List<CartItemResponse> itemList;
}
