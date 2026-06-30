package com.example.E_Commerce.Service;

import com.example.E_Commerce.DTO.CartDto;
import com.example.E_Commerce.DTO.CartItemRequestDto;

public interface CartService  {

    CartDto createCart();

    CartDto getCartById(Long id);

    String clearCart(Long id);

    CartDto addProductToCart(Long cartId,CartItemRequestDto cartItemRequestDto);

    CartDto removeProductFromCart(Long cartId, Long cartItemId);

    CartDto updateCartItemQuantity(Long cartId, Long cartItemId, int quantity);
}
