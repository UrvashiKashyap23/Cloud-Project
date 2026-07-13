package com.example.E_Commerce.Controller;


import com.example.E_Commerce.Request.CancelOrderRequest;
import com.example.E_Commerce.Request.OrderRequest;
import com.example.E_Commerce.Response.BaseApiResponse;
import com.example.E_Commerce.Response.OrderResponse;
import com.example.E_Commerce.DTO.Enums.StatusType;
import com.example.E_Commerce.Service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/create/{userId}")
    public BaseApiResponse<OrderResponse> createOrder(@PathVariable Long userId, @RequestBody OrderRequest orderRequest) {

        return orderService.createOrder(userId, orderRequest);
    }

    @GetMapping("/{userId}/{orderId}")
    public BaseApiResponse<OrderResponse> getOrderById(@PathVariable Long userId, @PathVariable Long orderId) {

        return orderService.getOrderById(userId, orderId);
    }

    @GetMapping("/getAll/{userId}")
    public BaseApiResponse<List<OrderResponse>> getAllOrdersByUser(@PathVariable Long userId) {

        return orderService.getAllOrdersByUser(userId);
    }

    @PutMapping("/update/{id}/{status}")
    public BaseApiResponse<OrderResponse> updateStatusById(
            @PathVariable Long id,
            @PathVariable StatusType status) {

        return orderService.updateStatusById(id, status);
    }

    @PutMapping("/cancel/{userId}/{orderId}")
    public BaseApiResponse<OrderResponse> cancelOrder(@PathVariable Long userId, @PathVariable Long orderId, @RequestBody CancelOrderRequest request){

        return orderService.cancelOrder(userId, orderId, request);
    }
}