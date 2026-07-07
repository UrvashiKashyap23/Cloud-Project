package com.example.E_Commerce.Service.Impl;

import com.example.E_Commerce.DTO.ProductRequestDto;
import com.example.E_Commerce.DTO.ProductResponseDto;
import com.example.E_Commerce.Entity.Category;
import com.example.E_Commerce.Entity.Product;
import com.example.E_Commerce.Repository.CategoryRepository;
import com.example.E_Commerce.Repository.ProductRepository;
import com.example.E_Commerce.Service.ProductService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    private final CategoryRepository categoryRepository;

    private final ModelMapper modelMapper;

    @Override
    public ProductResponseDto createProduct(ProductRequestDto productDto) {

        if (productDto.getFilledStockQuantity() > productDto.getStockQuantity()) {
            throw new IllegalArgumentException("Filled stock quantity cannot exceed stock quantity");
        }

        Category category = categoryRepository.findById(productDto.getCategoryId())
                .orElseThrow(() -> new RuntimeException(
                        "Category not found with id: " + productDto.getCategoryId()));


        Product product = modelMapper.map(productDto, Product.class);

        product.setProductId(null);

        product.setCategory(category);

        int remainingQuantity = productDto.getStockQuantity() - productDto.getFilledStockQuantity();
        product.setRemainingQuantity(remainingQuantity);
        product.setAvailable(remainingQuantity > 0);

        System.out.println("Product Id before save = " + product.getProductId());

        Product savedProduct = productRepository.save(product);

        return modelMapper.map(savedProduct, ProductResponseDto.class);
    }

    @Override
    public List<ProductResponseDto> getAllProducts() {

        List<Product> productList = productRepository.findAll();

        return productList
                .stream()
                .map((product) -> modelMapper.map(product, ProductResponseDto.class)).toList();
    }

    @Override
    public ProductResponseDto getProductById(Long id) {
        Product product = productRepository.findById(id).orElseThrow((() -> new RuntimeException("No element found with the id: " + id)));
        return modelMapper.map(product, ProductResponseDto.class);

    }

    @Override
    public ProductResponseDto updateProductById(Long id, ProductRequestDto productDto) {

        if (productDto.getFilledStockQuantity()
                > productDto.getStockQuantity()) {

            throw new IllegalArgumentException(
                    "Filled stock quantity cannot exceed stock quantity");
        }

        Product product = productRepository.findById(id).orElseThrow((() -> new RuntimeException("No element found with the id: " + id)));

        product.setName(productDto.getName());
        product.setPrice(productDto.getPrice());
        product.setDescription(productDto.getDescription());
        product.setStockQuantity(productDto.getStockQuantity());
        int remainingQuantity = productDto.getStockQuantity() - productDto.getFilledStockQuantity();
        product.setRemainingQuantity(remainingQuantity);
        product.setAvailable(remainingQuantity > 0);

        Product updatedDto = productRepository.save(product);

        return modelMapper.map(updatedDto, ProductResponseDto.class);

    }

    @Override
    public void deleteProductById(Long id) {

        if (!productRepository.existsById(id)) {
            throw new RuntimeException(
                    "No element found with the id: " + id);
        }

        productRepository.deleteById(id);

        System.out.println("Product with id " + id + " deleted successfully");
    }

    @Override
    public List<ProductResponseDto> getAvailableProducts() {
        List<Product> productList = productRepository.findProductByIsAvailableTrue();
        return productList.stream().map((product) -> modelMapper.map(product, ProductResponseDto.class)).toList();

    }
}
