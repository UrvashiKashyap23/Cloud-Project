package com.example.E_Commerce.Service;

import com.example.E_Commerce.DTO.OrderRequestDto;
import com.example.E_Commerce.Response.OrderResponseDto;
import com.example.E_Commerce.DTO.Enums.StatusType;

import java.util.List;

public interface OrderService {
    public OrderResponseDto createOrder(OrderRequestDto orderRequestDto);

    OrderResponseDto getOrderById(Long id);

    List<OrderResponseDto> getAllOrders();

    OrderResponseDto updateStatusById(Long id , StatusType statusType);
}
