package com.example.E_Commerce.Controller;

import com.example.E_Commerce.Request.ProductRequest;
import com.example.E_Commerce.Response.BaseApiResponse;
import com.example.E_Commerce.Response.ProductResponse;
import com.example.E_Commerce.Service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping("/create")
    public ResponseEntity<BaseApiResponse<ProductResponse>> createProduct(@Valid @RequestBody ProductRequest productRequest) {

        return ResponseEntity.ok(productService.createProduct(productRequest));
    }

    @GetMapping("/getAll")
    public ResponseEntity<BaseApiResponse<List<ProductResponse>>> getAllProducts() {

        return ResponseEntity.ok(productService.getAllProducts());
    }

    @GetMapping("/get/{productId}")
    public ResponseEntity<BaseApiResponse<ProductResponse>> getProductById(@PathVariable Long productId) {

        return ResponseEntity.ok(productService.getProductById(productId));
    }

    @GetMapping("/get/available")
    public ResponseEntity<BaseApiResponse<List<ProductResponse>>> getAvailableProducts() {

        return ResponseEntity.ok(productService.getAvailableProducts());
    }

    @PutMapping("/put/{productId}")
    public ResponseEntity<BaseApiResponse<ProductResponse>> updateProductById(@PathVariable Long productId, @Valid @RequestBody ProductRequest productRequest) {

        return ResponseEntity.ok(productService.updateProductById(productId, productRequest));
    }

    @DeleteMapping("/delete/{productId}")
    public ResponseEntity<BaseApiResponse<String>> deleteProductById(@PathVariable Long productId) {

        return ResponseEntity.ok(productService.deleteProductById(productId));
    }
}