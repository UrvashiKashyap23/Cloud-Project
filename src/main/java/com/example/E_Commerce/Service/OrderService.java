package com.example.E_Commerce.Service;

import com.example.E_Commerce.Request.CancelOrderRequest;
import com.example.E_Commerce.Request.OrderRequest;
import com.example.E_Commerce.Response.BaseApiResponse;
import com.example.E_Commerce.Response.OrderResponse;
import com.example.E_Commerce.DTO.Enums.StatusType;

import java.util.List;

public interface OrderService {

    BaseApiResponse<OrderResponse> createOrder(Long userId, OrderRequest orderRequest);

    BaseApiResponse<OrderResponse> getOrderById(Long userId, Long orderId);

    BaseApiResponse<List<OrderResponse>> getAllOrdersByUser(Long userId);

    BaseApiResponse<OrderResponse> updateStatusById(Long id, StatusType statusType);

    BaseApiResponse<OrderResponse> cancelOrder(Long userId, Long orderId, CancelOrderRequest request);
}