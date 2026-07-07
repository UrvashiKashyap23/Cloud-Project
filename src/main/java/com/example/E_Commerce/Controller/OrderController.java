package com.example.E_Commerce.Controller;


import com.example.E_Commerce.DTO.OrderRequestDto;
import com.example.E_Commerce.Response.OrderResponseDto;
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

    @PostMapping("/create")
    public OrderResponseDto createOrder(@RequestBody OrderRequestDto orderRequestDto){
        return orderService.createOrder(orderRequestDto);
    }

    @GetMapping("/{id}")
    public OrderResponseDto getOrderById(@PathVariable Long id){
        return orderService.getOrderById(id);
    }

    @GetMapping("/getAll")
    public List<OrderResponseDto> getAllOrders(){
        return orderService.getAllOrders();
    }

    @PutMapping("/update/{id}/{status}")
    public OrderResponseDto updateStatusById(@PathVariable Long id , @PathVariable StatusType status){
        return orderService.updateStatusById(id,status);
    }
}
