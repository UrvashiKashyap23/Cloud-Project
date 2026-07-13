package com.example.E_Commerce.Service.Impl;

import com.example.E_Commerce.Response.BaseApiResponse;
import com.example.E_Commerce.Response.CartResponse;
import com.example.E_Commerce.Request.CartItemRequest;
import com.example.E_Commerce.Response.CartItemResponse;
import com.example.E_Commerce.Entity.Cart;
import com.example.E_Commerce.Entity.CartItem;
import com.example.E_Commerce.Entity.Product;
import com.example.E_Commerce.Entity.User;
import com.example.E_Commerce.Repository.CartRepository;
import com.example.E_Commerce.Repository.ProductRepository;
import com.example.E_Commerce.Repository.UserRepository;
import com.example.E_Commerce.Service.CartService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CartServiceImpl implements CartService {

    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;
    private final CartRepository cartRepository;
    private final UserRepository userRepository;


   @Override
   public BaseApiResponse<CartResponse> getCart() {



        try {

            User user = getLoggedInUser();
            log.info("Fetching cart for user: {}", user.getUsername());

            Cart cart = cartRepository.findByUser(user)
                    .orElseThrow(() ->
                            new RuntimeException("Cart not found."));

            CartResponse response = convertToCartResponse(cart);

            return BaseApiResponse.<CartResponse>builder()
                    .code(200)
                    .message("Cart fetched successfully.")
                    .data(response)
                    .build();

        } catch (Exception e) {

            log.error("Error while fetching cart: {}", e.getMessage());

            return BaseApiResponse.<CartResponse>builder()
                    .code(500)
                    .message(e.getMessage())
                    .data(null)
                    .build();
        }
    }
    @Override
    public BaseApiResponse<String> clearCart() {


        try {

            User user = getLoggedInUser();
            log.info("Fetching cart for user: {}", user.getUsername());

            Cart cart = cartRepository.findByUser(user)
                    .orElseThrow(() ->
                            new RuntimeException("Cart not found."));

            cart.getItemList().clear();
            cart.setTotalAmount(0.0);

            cartRepository.save(cart);

            return BaseApiResponse.<String>builder()
                    .code(200)
                    .message("Cart cleared successfully.")
                    .data("Cart cleared successfully.")
                    .build();

        } catch (Exception e) {

            log.error("Error while clearing cart: {}", e.getMessage());

            return BaseApiResponse.<String>builder()
                    .code(500)
                    .message(e.getMessage())
                    .data(null)
                    .build();
        }
    }

    @Override
    @Transactional
    public BaseApiResponse<CartResponse> addProductToCart(CartItemRequest cartItemRequest) {



        try {

            User user = getLoggedInUser();

            log.info("Adding product {} to cart of user {}",
                    cartItemRequest.getProductId(), user.getUserId());

            Product product = productRepository.findById(cartItemRequest.getProductId())
                    .orElseThrow(() ->
                            new RuntimeException("Product not found."));

            if (!product.isAvailable()) {
                throw new RuntimeException("Product is currently unavailable.");
            }

            Cart cart = cartRepository.findByUser(user).orElse(null);

            if (cart == null) {

                cart = new Cart();
                cart.setUser(user);
                user.setCart(cart);
                cart.setTotalAmount(0.0);
                cart.setItemList(new ArrayList<>());
            }

            CartItem existingCartItem = cart.getItemList().stream()
                    .filter(item -> item.getProduct().getProductId()
                            .equals(product.getProductId()))
                    .findFirst()
                    .orElse(null);

            if (existingCartItem != null) {

                int updatedQuantity =
                        existingCartItem.getQuantity()
                                + cartItemRequest.getQuantity();

                if (updatedQuantity > product.getRemainingQuantity()) {
                    throw new RuntimeException("Insufficient stock.");
                }

                existingCartItem.setQuantity(updatedQuantity);

            } else {

                if (cartItemRequest.getQuantity()
                        > product.getRemainingQuantity()) {

                    throw new RuntimeException("Insufficient stock.");
                }

                CartItem cartItem = new CartItem();

                cartItem.setQuantity(cartItemRequest.getQuantity());
                cartItem.setProduct(product);
                cartItem.setCart(cart);

                cart.getItemList().add(cartItem);
            }

            double totalAmount = cart.getItemList().stream()
                    .mapToDouble(item ->
                            item.getProduct().getPrice() * item.getQuantity())
                    .sum();

            cart.setTotalAmount(totalAmount);

            Cart savedCart = cartRepository.save(cart);

            CartResponse response = convertToCartResponse(savedCart);

            return BaseApiResponse.<CartResponse>builder()
                    .code(200)
                    .message("Product added to cart successfully.")
                    .data(response)
                    .build();

        } catch (Exception e) {

            log.error("Error while adding product to cart: {}", e.getMessage());

            return BaseApiResponse.<CartResponse>builder()
                    .code(500)
                    .message(e.getMessage())
                    .data(null)
                    .build();
        }
    }
    @Override
    public BaseApiResponse<CartResponse> removeProductFromCart(Long cartItemId) {

        try {

            User user = getLoggedInUser();

            log.info("Removing cart item {} from user {}", cartItemId, user.getUsername());

            Cart cart = cartRepository.findByUser(user)
                    .orElseThrow(() ->
                            new RuntimeException("Cart not found."));

            CartItem cartItem = cart.getItemList().stream()
                    .filter(item -> item.getCartItemId().equals(cartItemId))
                    .findFirst()
                    .orElseThrow(() ->
                            new RuntimeException("Cart item not found."));

            cart.getItemList().remove(cartItem);

            double totalAmount = cart.getItemList().stream()
                    .mapToDouble(item ->
                            item.getProduct().getPrice() * item.getQuantity())
                    .sum();

            cart.setTotalAmount(totalAmount);

            Cart savedCart = cartRepository.save(cart);

            CartResponse response = convertToCartResponse(savedCart);

            return BaseApiResponse.<CartResponse>builder()
                    .code(200)
                    .message("Product removed from cart successfully.")
                    .data(response)
                    .build();

        } catch (Exception e) {

            log.error("Error while removing product from cart: {}", e.getMessage());

            return BaseApiResponse.<CartResponse>builder()
                    .code(500)
                    .message(e.getMessage())
                    .data(null)
                    .build();
        }
    }

    @Override
    public BaseApiResponse<CartResponse> updateCartItemQuantity(Long cartItemId, int quantity) {

        try {

            User user = getLoggedInUser();

            log.info("Updating quantity of cart item {} for user {} to {}", cartItemId, user.getUsername(), quantity);
            Cart cart = cartRepository.findByUser(user)
                    .orElseThrow(() ->
                            new RuntimeException("Cart not found."));

            CartItem cartItem = cart.getItemList().stream()
                    .filter(item -> item.getCartItemId().equals(cartItemId))
                    .findFirst()
                    .orElseThrow(() ->
                            new RuntimeException("Cart item not found."));

            if (quantity <= 0) {
                throw new RuntimeException("Quantity must be greater than zero.");
            }

            if (quantity == cartItem.getQuantity()) {
                throw new RuntimeException("New quantity must be different from the existing quantity.");
            }

            if (quantity > cartItem.getProduct().getRemainingQuantity()) {
                throw new RuntimeException("Insufficient stock.");
            }

            cartItem.setQuantity(quantity);

            double totalAmount = cart.getItemList().stream()
                    .mapToDouble(item ->
                            item.getProduct().getPrice() * item.getQuantity())
                    .sum();

            cart.setTotalAmount(totalAmount);

            Cart savedCart = cartRepository.save(cart);

            CartResponse response = convertToCartResponse(savedCart);

            return BaseApiResponse.<CartResponse>builder()
                    .code(200)
                    .message("Cart item quantity updated successfully.")
                    .data(response)
                    .build();

        } catch (Exception e) {

            log.error("Error while updating cart item quantity: {}", e.getMessage());

            return BaseApiResponse.<CartResponse>builder()
                    .code(500)
                    .message(e.getMessage())
                    .data(null)
                    .build();
        }
    }

    private User getLoggedInUser() {

        Authentication authentication = SecurityContextHolder
                .getContext()
                .getAuthentication();

        String username = authentication.getName();

        return userRepository.findByUsername(username)
                .orElseThrow(() ->
                        new RuntimeException("User not found."));
    }

    private CartResponse convertToCartResponse(Cart cart) {

        List<CartItemResponse> items = cart.getItemList().stream()
                .map(item -> CartItemResponse.builder()
                        .cartItemId(item.getCartItemId())
                        .productId(item.getProduct().getProductId())
                        .productName(item.getProduct().getName())
                        .productPrice(item.getProduct().getPrice())
                        .quantity(item.getQuantity())
                        .totalPrice(item.getProduct().getPrice() * item.getQuantity())
                        .build())
                .toList();

        return CartResponse.builder()
                .cartId(cart.getCartId())
                .totalAmount(cart.getTotalAmount())
                .itemList(items)
                .build();
    }
}


