package com.example.E_Commerce.Controller;

import com.example.E_Commerce.DTO.ProductRequestDto;
import com.example.E_Commerce.DTO.ProductResponseDto;
import com.example.E_Commerce.Service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping("/create")
    public ProductResponseDto createProduct(@RequestBody ProductRequestDto productDto){
        return productService.createProduct(productDto);

    }

    @GetMapping("/getAll")
    public List<ProductResponseDto> getAllProducts(){
        return productService.getAllProducts();
    }

    @GetMapping("/get/{id}")
    public ProductResponseDto getProductById(@PathVariable Long id){
        return productService.getProductById(id);
    }

    @GetMapping("/get/available")
    public List<ProductResponseDto> getAvailableProducts(){
        return productService.getAvailableProducts();
    }

    @PutMapping("/put/{id}")
    public ProductResponseDto updateProductById(@PathVariable Long id, @RequestBody ProductRequestDto productDto){
        return productService.updateProductById(id,productDto);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteProductById(@PathVariable Long id,@RequestBody ProductRequestDto productRequestDto){
        productService.deleteProductById(id);
    }
}
