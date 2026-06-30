package com.example.E_Commerce.DTO;

import lombok.*;

import javax.annotation.processing.Generated;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CartDto {

    private Long cartId;

    private Double totalAmount;

    private List<CartItemResponseDto> itemList;
}
