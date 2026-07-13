package com.example.E_Commerce.Request;

import com.example.E_Commerce.DTO.OrderItemRequestDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderRequest {

    private List<OrderItemRequestDto> items;
}
