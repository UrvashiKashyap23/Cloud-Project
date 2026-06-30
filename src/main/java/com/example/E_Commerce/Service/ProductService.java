package com.example.E_Commerce.Service;


import com.example.E_Commerce.DTO.ProductRequestDto;
import com.example.E_Commerce.DTO.ProductResponseDto;

import java.util.List;

public interface ProductService {

    ProductResponseDto createProduct(ProductRequestDto productRequestDto);

    List<ProductResponseDto> getAllProducts();

    ProductResponseDto getProductById(Long id);

    ProductResponseDto updateProductById(Long id, ProductRequestDto productDto);

    void deleteProductById(Long id);

    List<ProductResponseDto> getAvailableProducts();
}
