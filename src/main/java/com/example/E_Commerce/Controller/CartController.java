package com.example.E_Commerce.Controller;

import com.example.E_Commerce.DTO.CartDto;
import com.example.E_Commerce.DTO.CartItemRequestDto;
import com.example.E_Commerce.Service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @PostMapping("/create")
    public CartDto createCart(){
        return cartService.createCart();
    }

    @GetMapping("/get/{cartId}")
    public CartDto getCartById(@PathVariable Long cartId){
        return cartService.getCartById(cartId);
    }

    @DeleteMapping("/clear/{id}")
    public String clearCart(@PathVariable Long id){
        return cartService.clearCart(id);

    }
    @PostMapping("/addproduct/{cartId}")
    public CartDto addProductToCart(@PathVariable Long cartId,@RequestBody  CartItemRequestDto cartItemRequestDto){
        return cartService.addProductToCart(cartId,cartItemRequestDto);
    }
    @DeleteMapping("/deleteCartItem/{cartId}/{cartItemId}")
    public CartDto deleteProductFromCart(@PathVariable Long cartId , @PathVariable Long cartItemId){
        return cartService.removeProductFromCart(cartId, cartItemId);
    }
    @PutMapping("/{cartId}/{cartItemId}/{quantity}")
    public CartDto updateCartItemQuantity(@PathVariable Long cartId, @PathVariable Long cartItemId, @PathVariable int quantity) {

        return cartService.updateCartItemQuantity(cartId, cartItemId, quantity);
    }
}
