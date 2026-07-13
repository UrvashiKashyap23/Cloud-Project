package com.example.E_Commerce.Service.Impl;

import com.example.E_Commerce.DTO.OrderItemRequestDto;
import com.example.E_Commerce.DTO.OrderItemResponseDto;
import com.example.E_Commerce.Entity.User;
import com.example.E_Commerce.Repository.UserRepository;
import com.example.E_Commerce.Request.CancelOrderRequest;
import com.example.E_Commerce.Request.OrderRequest;
import com.example.E_Commerce.Response.BaseApiResponse;
import com.example.E_Commerce.Response.OrderResponse;
import com.example.E_Commerce.DTO.Enums.StatusType;
import com.example.E_Commerce.Entity.Order;
import com.example.E_Commerce.Entity.OrderItem;
import com.example.E_Commerce.Entity.Product;
import com.example.E_Commerce.Repository.OrderRepository;
import com.example.E_Commerce.Repository.ProductRepository;
import com.example.E_Commerce.Service.OrderService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    private final ProductRepository productRepository;

    private final UserRepository userRepository;

    private final ModelMapper modelMapper;

    @Override
    @Transactional
    public BaseApiResponse<OrderResponse> createOrder(Long userId, OrderRequest requestDto) {

        log.info("Creating order for user: {}", userId);

        try {

            User user = userRepository.findById(userId)
                    .orElseThrow(() ->
                            new RuntimeException("User not found."));

            validateRequest(requestDto);

            Order order = new Order();
            order.setUser(user);
            order.setStatus(StatusType.PENDING);

            List<OrderItem> orderItems = new ArrayList<>();
            double totalAmount = 0;

            for (OrderItemRequestDto itemDto : requestDto.getItems()) {

                Product product = getValidProduct(itemDto);

                updateStock(product, itemDto.getQuantity());

                OrderItem orderItem = buildOrderItem(
                        order,
                        product,
                        itemDto.getQuantity());

                totalAmount += orderItem.getPriceAtPurchase()
                        * itemDto.getQuantity();

                orderItems.add(orderItem);
            }

            order.setOrderItems(orderItems);
            order.setTotalAmount(totalAmount);

            Order savedOrder = orderRepository.save(order);

            OrderResponse response = mapToResponseDto(savedOrder);

            return BaseApiResponse.<OrderResponse>builder()
                    .code(200)
                    .message("Order created successfully.")
                    .data(response)
                    .build();

        } catch (Exception e) {

            log.error("Error while creating order: {}", e.getMessage());

            return BaseApiResponse.<OrderResponse>builder()
                    .code(500)
                    .message(e.getMessage())
                    .data(null)
                    .build();
        }
    }


    private void validateRequest(OrderRequest requestDto) {
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

    private OrderResponse mapToResponseDto(Order order) {

        List<OrderItemResponseDto> items = order.getOrderItems().stream()
                .map(i -> new OrderItemResponseDto(
                        i.getProduct().getProductId(),
                        i.getProduct().getName(),
                        i.getQuantity(),
                        i.getPriceAtPurchase(),
                        i.getPriceAtPurchase() * i.getQuantity()
                ))
                .toList();

        return new OrderResponse(
                order.getId(),
                order.getTotalAmount(),
                order.getStatus(),
                order.getOrderDate(),
                order.getCreatedAt(),
                order.getUpdatedAt(),
                order.getCancelReason(),
                items
        );
    }



    @Override
    public BaseApiResponse<OrderResponse> getOrderById(Long userId, Long orderId) {

        log.info("Fetching order {} for user {}", orderId, userId);

        try {

            User user = userRepository.findById(userId)
                    .orElseThrow(() ->
                            new RuntimeException("User not found."));

            Order order = orderRepository.findById(orderId)
                    .orElseThrow(() ->
                            new RuntimeException("Order not found."));

            if (!order.getUser().getUserId().equals(user.getUserId())) {
                throw new RuntimeException("You are not authorized to access this order.");
            }

            OrderResponse response = mapToResponseDto(order);

            return BaseApiResponse.<OrderResponse>builder()
                    .code(200)
                    .message("Order fetched successfully.")
                    .data(response)
                    .build();

        } catch (Exception e) {

            log.error("Error while fetching order: {}", e.getMessage());

            return BaseApiResponse.<OrderResponse>builder()
                    .code(500)
                    .message(e.getMessage())
                    .data(null)
                    .build();
        }
    }

    @Override
    public BaseApiResponse<List<OrderResponse>> getAllOrdersByUser(Long userId) {

        log.info("Fetching all orders for user: {}", userId);

        try {

            User user = userRepository.findById(userId)
                    .orElseThrow(() ->
                            new RuntimeException("User not found."));

            List<Order> orders = orderRepository.findByUser(user);

            List<OrderResponse> response = orders.stream()
                    .map(this::mapToResponseDto)
                    .toList();

            return BaseApiResponse.<List<OrderResponse>>builder()
                    .code(200)
                    .message("Orders fetched successfully.")
                    .data(response)
                    .build();

        } catch (Exception e) {

            log.error("Error while fetching orders: {}", e.getMessage());

            return BaseApiResponse.<List<OrderResponse>>builder()
                    .code(500)
                    .message(e.getMessage())
                    .data(null)
                    .build();
        }
    }

    @Override
    @Transactional
    public BaseApiResponse<OrderResponse> updateStatusById(Long id, StatusType statusType) {

        log.info("Updating status of order {} to {}", id, statusType);

        try {

            Order order = orderRepository.findById(id)
                    .orElseThrow(() ->
                            new RuntimeException("Order not found."));

            order.setStatus(statusType);

            Order savedOrder = orderRepository.save(order);

            OrderResponse response = mapToResponseDto(savedOrder);

            return BaseApiResponse.<OrderResponse>builder()
                    .code(200)
                    .message("Order status updated successfully.")
                    .data(response)
                    .build();

        } catch (Exception e) {

            log.error("Error while updating order status: {}", e.getMessage());

            return BaseApiResponse.<OrderResponse>builder()
                    .code(500)
                    .message(e.getMessage())
                    .data(null)
                    .build();
        }
    }

    @Override
    @Transactional
    public BaseApiResponse<OrderResponse> cancelOrder(Long userId, Long orderId, CancelOrderRequest request) {

        log.info("Cancelling order {} for user {}", orderId, userId);

        try {

            User user = userRepository.findById(userId)
                    .orElseThrow(() ->
                            new RuntimeException("User not found."));

            Order order = orderRepository.findById(orderId)
                    .orElseThrow(() ->
                            new RuntimeException("Order not found."));

            if (!order.getUser().getUserId().equals(user.getUserId())) {
                throw new RuntimeException("You are not authorized to cancel this order.");
            }

            if (order.getStatus() == StatusType.DELIVERED) {
                throw new RuntimeException("Delivered order cannot be cancelled.");
            }

            if (order.getStatus() == StatusType.CANCELLED) {
                throw new RuntimeException("Order is already cancelled.");
            }

            order.setStatus(StatusType.CANCELLED);

            order.setCancelReason(request.getCancelReason());

            Order savedOrder = orderRepository.save(order);

            OrderResponse response = mapToResponseDto(savedOrder);

            return BaseApiResponse.<OrderResponse>builder()
                    .code(200)
                    .message("Order cancelled successfully.")
                    .data(response)
                    .build();

        } catch (Exception e) {

            log.error("Error while cancelling order: {}", e.getMessage());

            return BaseApiResponse.<OrderResponse>builder()
                    .code(500)
                    .message(e.getMessage())
                    .data(null)
                    .build();
        }
    }
}
