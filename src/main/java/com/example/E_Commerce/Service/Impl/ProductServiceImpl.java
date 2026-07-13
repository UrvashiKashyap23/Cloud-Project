package com.example.E_Commerce.Service.Impl;

import com.example.E_Commerce.Request.ProductRequest;
import com.example.E_Commerce.Response.BaseApiResponse;
import com.example.E_Commerce.Response.ProductResponse;
import com.example.E_Commerce.Entity.Category;
import com.example.E_Commerce.Entity.Product;
import com.example.E_Commerce.Repository.CategoryRepository;
import com.example.E_Commerce.Repository.ProductRepository;
import com.example.E_Commerce.Service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
@Slf4j
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;

    @Override
    public BaseApiResponse<ProductResponse> createProduct(ProductRequest productRequest) {

        log.info("Creating product: {}", productRequest.getName());

        try {

            if (productRequest.getFilledStockQuantity() > productRequest.getStockQuantity()) {
                throw new IllegalArgumentException("Filled stock quantity cannot exceed stock quantity.");
            }

            Category category = categoryRepository.findById(productRequest.getCategoryId())
                    .orElseThrow(() -> new RuntimeException(
                            "Category not found with id: " + productRequest.getCategoryId()));

            Product product = modelMapper.map(productRequest, Product.class);

            product.setProductId(null);
            product.setCategory(category);

            int remainingQuantity = productRequest.getStockQuantity()
                    - productRequest.getFilledStockQuantity();

            product.setRemainingQuantity(remainingQuantity);
            product.setAvailable(remainingQuantity > 0);

            Product savedProduct = productRepository.save(product);

            ProductResponse response =
                    modelMapper.map(savedProduct, ProductResponse.class);

            return BaseApiResponse.<ProductResponse>builder()
                    .code(201)
                    .message("Product created successfully.")
                    .data(response)
                    .build();

        } catch (Exception e) {

            log.error("Error while creating product: {}", e.getMessage());

            return BaseApiResponse.<ProductResponse>builder()
                    .code(500)
                    .message(e.getMessage())
                    .data(null)
                    .build();
        }
    }

    @Override
    public BaseApiResponse<List<ProductResponse>> getAllProducts() {

        log.info("Fetching all products.");

        try {

            List<ProductResponse> response = productRepository.findAll()
                    .stream()
                    .map(product -> modelMapper.map(product, ProductResponse.class))
                    .toList();

            return BaseApiResponse.<List<ProductResponse>>builder()
                    .code(200)
                    .message("Products fetched successfully.")
                    .data(response)
                    .build();

        } catch (Exception e) {

            log.error("Error while fetching products: {}", e.getMessage());

            return BaseApiResponse.<List<ProductResponse>>builder()
                    .code(500)
                    .message(e.getMessage())
                    .data(null)
                    .build();
        }
    }

    @Override
    public BaseApiResponse<ProductResponse> getProductById(Long productId) {

        log.info("Fetching product with id: {}", productId);

        try {

            Product product = productRepository.findById(productId)
                    .orElseThrow(() ->
                            new RuntimeException("Product not found with id: " + productId));

            ProductResponse response =
                    modelMapper.map(product, ProductResponse.class);

            return BaseApiResponse.<ProductResponse>builder()
                    .code(200)
                    .message("Product fetched successfully.")
                    .data(response)
                    .build();

        } catch (Exception e) {

            log.error("Error while fetching product: {}", e.getMessage());

            return BaseApiResponse.<ProductResponse>builder()
                    .code(500)
                    .message(e.getMessage())
                    .data(null)
                    .build();
        }
    }

    @Override
    public BaseApiResponse<ProductResponse> updateProductById(Long productId,
                                                              ProductRequest productRequest) {

        log.info("Updating product with id: {}", productId);

        try {

            if (productRequest.getFilledStockQuantity() > productRequest.getStockQuantity()) {
                throw new IllegalArgumentException("Filled stock quantity cannot exceed stock quantity.");
            }

            Product product = productRepository.findById(productId)
                    .orElseThrow(() ->
                            new RuntimeException("Product not found with id: " + productId));

            Category category = categoryRepository.findById(productRequest.getCategoryId())
                    .orElseThrow(() ->
                            new RuntimeException("Category not found with id: "
                                    + productRequest.getCategoryId()));

            product.setName(productRequest.getName());
            product.setPrice(productRequest.getPrice());
            product.setDescription(productRequest.getDescription());
            product.setStockQuantity(productRequest.getStockQuantity());
            product.setCategory(category);

            int remainingQuantity = productRequest.getStockQuantity()
                    - productRequest.getFilledStockQuantity();

            product.setRemainingQuantity(remainingQuantity);
            product.setAvailable(remainingQuantity > 0);

            Product updatedProduct = productRepository.save(product);

            ProductResponse response =
                    modelMapper.map(updatedProduct, ProductResponse.class);

            return BaseApiResponse.<ProductResponse>builder()
                    .code(200)
                    .message("Product updated successfully.")
                    .data(response)
                    .build();

        } catch (Exception e) {

            log.error("Error while updating product: {}", e.getMessage());

            return BaseApiResponse.<ProductResponse>builder()
                    .code(500)
                    .message(e.getMessage())
                    .data(null)
                    .build();
        }
    }

    @Override
    public BaseApiResponse<String> deleteProductById(Long productId) {

        log.info("Deleting product with id: {}", productId);

        try {

            if (!productRepository.existsById(productId)) {
                throw new RuntimeException("Product not found with id: " + productId);
            }

            productRepository.deleteById(productId);

            return BaseApiResponse.<String>builder()
                    .code(200)
                    .message("Product deleted successfully.")
                    .data("Deleted")
                    .build();

        } catch (Exception e) {

            log.error("Error while deleting product: {}", e.getMessage());

            return BaseApiResponse.<String>builder()
                    .code(500)
                    .message(e.getMessage())
                    .data(null)
                    .build();
        }
    }

    @Override
    public BaseApiResponse<List<ProductResponse>> getAvailableProducts() {

        log.info("Fetching available products.");

        try {

            List<ProductResponse> response = productRepository.findProductByIsAvailableTrue()
                    .stream()
                    .map(product -> modelMapper.map(product, ProductResponse.class))
                    .toList();

            return BaseApiResponse.<List<ProductResponse>>builder()
                    .code(200)
                    .message("Available products fetched successfully.")
                    .data(response)
                    .build();

        } catch (Exception e) {

            log.error("Error while fetching available products: {}", e.getMessage());

            return BaseApiResponse.<List<ProductResponse>>builder()
                    .code(500)
                    .message(e.getMessage())
                    .data(null)
                    .build();
        }
    }
}