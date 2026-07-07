package com.example.E_Commerce.Service.Impl;

import com.example.E_Commerce.DTO.CartDto;
import com.example.E_Commerce.DTO.CartItemRequestDto;
import com.example.E_Commerce.DTO.CartItemResponseDto;
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
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;
    private final CartRepository cartRepository;
    private final UserRepository userRepository;

    @Override
    public CartDto createCart() {
        Cart cart = new Cart();
        cart.setTotalAmount(0.0);
        cart.setItemList(new ArrayList<>());
        Cart savedCart = cartRepository.save(cart);
        return modelMapper.map(savedCart,CartDto.class);

    }

    @Override
    public CartDto getCartById(Long id) {
        Cart cart = cartRepository.findById(id).orElseThrow(()->new RuntimeException("Cart not found"));
        CartDto cartDto = new CartDto();
        cartDto.setCartId(cart.getCartId());
        cartDto.setTotalAmount(cart.getTotalAmount());

        List<CartItemResponseDto> items = cart.getItemList().stream()
                .map(item -> new CartItemResponseDto(
                        item.getCartItemId(),
                        item.getProduct().getProductId(),
                        item.getProduct().getName(),
                        item.getProduct().getPrice(),
                        item.getQuantity(),
                        item.getProduct().getPrice() * item.getQuantity()
                ))
                .toList();

        cartDto.setItemList(items);

        return  cartDto;

    }

    @Override
    public String clearCart(Long id) {
        Cart cart = cartRepository.findById(id).orElseThrow(()->new RuntimeException("Cart with this id does not exist"));

        cart.getItemList().clear();

        cart.setTotalAmount(0.0);

        cartRepository.save(cart);

        return "Cart cleared successfully!!";

    }

    @Override
    @Transactional
    public CartDto addProductToCart(Long userId, CartItemRequestDto cartItemRequestDto) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found!")); // is user logined

        Product product = productRepository.findById(cartItemRequestDto.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found!!"));

        if (!product.isAvailable()) {
            throw new RuntimeException("Product is currently unavailable!");
        }

        Cart cart = cartRepository.findByUser(user).orElse(null);

        if(cart == null){
            cart = new Cart();
            cart.setUser(user);
            user.setCart(cart);
            cart.setTotalAmount(0.0);
            cart.setItemList(new ArrayList<>());
        }
        CartItem existingCartItem = cart.getItemList().stream()
                .filter(item -> item.getProduct().getProductId().equals(product.getProductId()))
                .findFirst()
                .orElse(null);

        if (existingCartItem != null) {

            int sum = existingCartItem.getQuantity() + cartItemRequestDto.getQuantity();

            if (sum > product.getRemainingQuantity()) {
                throw new RuntimeException("Insufficient stock!");
            }

            existingCartItem.setQuantity(sum);

        } else {

            if (cartItemRequestDto.getQuantity() > product.getRemainingQuantity()) {
                throw new RuntimeException("Insufficient stock!");
            }

            CartItem cartItem = new CartItem();

            cartItem.setQuantity(cartItemRequestDto.getQuantity());
            cartItem.setProduct(product);
            cartItem.setCart(cart);

            cart.getItemList().add(cartItem);
        }

        // Recalculate total amount
        double totalAmount = cart.getItemList().stream()
                .mapToDouble(item ->
                        item.getProduct().getPrice() * item.getQuantity())
                .sum();

        cart.setTotalAmount(totalAmount);

        cart.setUser(user);

        Cart savedCart = cartRepository.save(cart);

        return convertTocartDto(savedCart);
    }

    @Override
    public CartDto removeProductFromCart(Long cartId, Long cartItemId) {
        Cart cart = cartRepository.findById(cartId).orElseThrow(() -> new RuntimeException("Cart not found!!"));
        CartItem cartItem = cart.getItemList().stream().filter(item -> item.getCartItemId().equals(cartItemId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("cartItem not found!!"));

        cart.getItemList().remove(cartItem);

        double totalAmount = cart.getItemList().stream()
                .mapToDouble(item ->
                        item.getProduct().getPrice() * item.getQuantity())
                .sum();

        cart.setTotalAmount(totalAmount);

        Cart savedCart = cartRepository.save(cart);

        return convertTocartDto(savedCart);

    }

    @Override
    public CartDto updateCartItemQuantity(Long cartId, Long cartItemId, int quantity) {
        Cart cart = cartRepository.findById(cartId).orElseThrow(() -> new RuntimeException("Cart not found!!"));

        CartItem cartItem = cart.getItemList().stream().filter(item -> item.getCartItemId().equals(cartItemId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("cartItem not found!!"));

        if(quantity<0)throw new RuntimeException("quantity must be greater than zero!");

        if(quantity >cartItem.getProduct().getRemainingQuantity()){
            throw new RuntimeException("Insufficient stock!");
        }

        cartItem.setQuantity(quantity);

        double totalAmount = cart.getItemList().stream()
                .mapToDouble(item ->
                        item.getProduct().getPrice() * item.getQuantity())
                .sum();

        cart.setTotalAmount(totalAmount);

        Cart savedCart = cartRepository.save(cart);

        return convertTocartDto(savedCart);
    }

    private CartDto convertTocartDto(Cart savedCart){
        List<CartItemResponseDto> items = savedCart.getItemList().stream()
                .map(item -> new CartItemResponseDto(
                        item.getCartItemId(),
                        item.getProduct().getProductId(),
                        item.getProduct().getName(),
                        item.getProduct().getPrice(),
                        item.getQuantity(),
                        item.getProduct().getPrice() * item.getQuantity()
                ))
                .toList();

        CartDto cartDto = new CartDto();
        cartDto.setCartId(savedCart.getCartId());
        cartDto.setTotalAmount(savedCart.getTotalAmount());
        cartDto.setItemList(items);

        return cartDto;
    }
}


