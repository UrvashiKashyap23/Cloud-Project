package com.example.E_Commerce.Service;


import com.example.E_Commerce.Request.ProductRequest;
import com.example.E_Commerce.Response.BaseApiResponse;
import com.example.E_Commerce.Response.ProductResponse;

import java.util.List;

public interface ProductService {

    BaseApiResponse<ProductResponse> createProduct(ProductRequest productRequest);

    BaseApiResponse<List<ProductResponse>> getAllProducts();

    BaseApiResponse<ProductResponse> getProductById(Long productId);

    BaseApiResponse<ProductResponse> updateProductById(Long productId, ProductRequest productRequest);

    BaseApiResponse<String> deleteProductById(Long productId);

    BaseApiResponse<List<ProductResponse>> getAvailableProducts();
}