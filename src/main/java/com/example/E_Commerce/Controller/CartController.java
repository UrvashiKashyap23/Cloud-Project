package com.example.E_Commerce.Controller;

import com.example.E_Commerce.Response.BaseApiResponse;
import com.example.E_Commerce.Response.CartResponse;
import com.example.E_Commerce.Request.CartItemRequest;
import com.example.E_Commerce.Service.CartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @GetMapping("/get")
    public ResponseEntity<BaseApiResponse<CartResponse>> getCart() {
        return ResponseEntity.ok(cartService.getCart());
    }

    @DeleteMapping("/clear")
    public ResponseEntity<BaseApiResponse<String>> clearCart() {
        return ResponseEntity.ok(cartService.clearCart());
    }

    @PostMapping("/addproduct")
    public ResponseEntity<BaseApiResponse<CartResponse>> addProductToCart(@Valid @RequestBody CartItemRequest cartItemRequest) {

        return ResponseEntity.ok(
                cartService.addProductToCart(cartItemRequest));
    }

    @DeleteMapping("/deleteCartItem/{cartItemId}")
    public ResponseEntity<BaseApiResponse<CartResponse>> deleteProductFromCart(@PathVariable Long cartItemId) {

        return ResponseEntity.ok(
                cartService.removeProductFromCart(cartItemId));
    }

    @PutMapping("/{cartItemId}/{quantity}")
    public ResponseEntity<BaseApiResponse<CartResponse>> updateCartItemQuantity(@PathVariable Long cartItemId, @PathVariable int quantity) {

        return ResponseEntity.ok(
                cartService.updateCartItemQuantity(cartItemId, quantity));
    }
}