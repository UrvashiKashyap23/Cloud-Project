package com.example.E_Commerce.Service;

import com.example.E_Commerce.Response.BaseApiResponse;
import com.example.E_Commerce.Response.CartResponse;
import com.example.E_Commerce.Request.CartItemRequest;

public interface CartService {

    BaseApiResponse<CartResponse> getCart();

    BaseApiResponse<String> clearCart();

    BaseApiResponse<CartResponse> addProductToCart(CartItemRequest cartItemRequest);

    BaseApiResponse<CartResponse> removeProductFromCart(Long cartItemId);

    BaseApiResponse<CartResponse> updateCartItemQuantity(Long cartItemId, int quantity);

}