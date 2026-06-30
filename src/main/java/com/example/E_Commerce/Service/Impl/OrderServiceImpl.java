package com.example.E_Commerce.Service.Impl;

import com.example.E_Commerce.DTO.OrderItemRequestDto;
import com.example.E_Commerce.DTO.OrderItemResponseDto;
import com.example.E_Commerce.DTO.OrderRequestDto;
import com.example.E_Commerce.DTO.OrderResponseDto;
import com.example.E_Commerce.DTO.type.StatusType;
import com.example.E_Commerce.Entity.Order;
import com.example.E_Commerce.Entity.OrderItem;
import com.example.E_Commerce.Entity.Product;
import com.example.E_Commerce.Repository.CategoryRepository;
import com.example.E_Commerce.Repository.OrderRepository;
import com.example.E_Commerce.Repository.ProductRepository;
import com.example.E_Commerce.Service.OrderService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    private final ProductRepository productRepository;

    private final ModelMapper modelMapper;
    @Override
    @Transactional
    public OrderResponseDto createOrder(OrderRequestDto requestDto) {

        validateRequest(requestDto);

        Order order = new Order();
        order.setStatus(StatusType.PENDING);

        List<OrderItem> orderItems = new ArrayList<>();
        double totalAmount = 0;

        for (OrderItemRequestDto itemDto : requestDto.getItems()) {

            Product product = getValidProduct(itemDto);

            updateStock(product, itemDto.getQuantity());

            OrderItem orderItem = buildOrderItem(order, product, itemDto.getQuantity());

            totalAmount += orderItem.getPriceAtPurchase() * itemDto.getQuantity();

            orderItems.add(orderItem);
        }

        order.setOrderItems(orderItems);
        order.setTotalAmount(totalAmount);

        return mapToResponseDto(orderRepository.save(order));
    }



    private void validateRequest(OrderRequestDto requestDto) {
        if (requestDto.getItems() == null || requestDto.getItems().isEmpty()) { // StringUtils apache.common.lang StringUtils.isBlank(requestDto.getItems)
            throw new IllegalArgumentException("Order must contain at least one item");
        }
    }

    private Product getValidProduct(OrderItemRequestDto itemDto) {
        Product product = productRepository.findById(itemDto.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found: " + itemDto.getProductId()));

        if (!product.isAvailable() || product.getRemainingQuantity() < itemDto.getQuantity()) {
            throw new RuntimeException("Insufficient stock for: " + product.getName());
        }

        return product;
    }

    private void updateStock(Product product, int quantity) {
        product.setRemainingQuantity(product.getRemainingQuantity() - quantity);

        if (product.getRemainingQuantity() == 0) {
            product.setAvailable(false);
        }

        productRepository.save(product);
    }

    private OrderItem buildOrderItem(Order order, Product product, int qty) {
        OrderItem item = new OrderItem();
        item.setOrder(order);
        item.setProduct(product);
        item.setQuantity(qty);
        item.setPriceAtPurchase(product.getPrice());
        return item;
    }

    private OrderResponseDto mapToResponseDto(Order order) {

        List<OrderItemResponseDto> items = order.getOrderItems().stream()
                .map(i -> new OrderItemResponseDto(
                        i.getProduct().getId(),
                        i.getProduct().getName(),
                        i.getQuantity(),
                        i.getPriceAtPurchase(),
                        i.getPriceAtPurchase() * i.getQuantity()
                ))
                .toList();

        return new OrderResponseDto(
                order.getId(),
                order.getTotalAmount(),
                order.getStatus(),
                order.getOrderDate(),
                order.getCreatedAt(),
                order.getUpdatedAt(),
                items
        );
    }


    @Override
    public OrderResponseDto getOrderById(Long id) {
        Order order = orderRepository.findById(id).orElseThrow();
        return modelMapper.map(order,OrderResponseDto.class);
    }

    @Override
    public List<OrderResponseDto> getAllOrders() {
        List<Order> orderList = orderRepository.findAll();
        return orderList.stream().map((order) -> modelMapper.map(order, OrderResponseDto.class)).toList();
    }

    @Override
    public OrderResponseDto updateStatusById(Long id , StatusType statusType) {
        Order order = orderRepository.findById(id).orElseThrow();
        order.setStatus(statusType);
        Order savedOrder = orderRepository.save(order);
        return modelMapper.map(savedOrder,OrderResponseDto.class);
    }
}
