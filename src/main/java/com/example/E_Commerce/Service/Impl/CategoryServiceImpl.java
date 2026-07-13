package com.example.E_Commerce.Service.Impl;

import com.example.E_Commerce.Entity.Category;
import com.example.E_Commerce.Repository.CategoryRepository;
import com.example.E_Commerce.Response.BaseApiResponse;
import com.example.E_Commerce.Request.CategoryRequest;
import com.example.E_Commerce.Response.CategoryResponse;
import com.example.E_Commerce.Service.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;

    @Override
    public BaseApiResponse<CategoryResponse> createCategory(CategoryRequest categoryRequest) {

        log.info("Creating category with name: {}", categoryRequest.getName());

        try {
            Category category = modelMapper.map(categoryRequest, Category.class);

            Category savedCategory = categoryRepository.save(category);

            CategoryResponse response =
                    modelMapper.map(savedCategory, CategoryResponse.class);

            return BaseApiResponse.<CategoryResponse>builder()
                    .code(201)
                    .message("Category created successfully.")
                    .data(response)
                    .build();

        } catch (Exception e) {

            log.error("Error while creating category: {}", e.getMessage());

            return BaseApiResponse.<CategoryResponse>builder()
                    .code(500)
                    .message("Failed to create category.")
                    .data(null)
                    .build();
        }
    }

    @Override
    public BaseApiResponse<List<CategoryResponse>> getAllCategories() {

        log.info("Fetching all categories.");

        try {

            List<Category> categories = categoryRepository.findAll();

            List<CategoryResponse> response = categories.stream()
                    .map(category -> modelMapper.map(category, CategoryResponse.class))
                    .toList();

            return BaseApiResponse.<List<CategoryResponse>>builder()
                    .code(200)
                    .message("Categories fetched successfully.")
                    .data(response)
                    .build();

        } catch (Exception e) {

            log.error("Error while fetching categories: {}", e.getMessage());

            return BaseApiResponse.<List<CategoryResponse>>builder()
                    .code(500)
                    .message("Failed to fetch categories.")
                    .data(null)
                    .build();
        }
    }

    @Override
    public BaseApiResponse<CategoryResponse> getCategoryById(Long categoryId) {

        log.info("Fetching category with id: {}", categoryId);

        try {

            Category category = categoryRepository.findById(categoryId)
                    .orElseThrow(() ->
                            new RuntimeException("Category not found with id: " + categoryId));

            CategoryResponse response =
                    modelMapper.map(category, CategoryResponse.class);

            return BaseApiResponse.<CategoryResponse>builder()
                    .code(200)
                    .message("Category fetched successfully.")
                    .data(response)
                    .build();

        } catch (Exception e) {

            log.error("Error while fetching category: {}", e.getMessage());

            return BaseApiResponse.<CategoryResponse>builder()
                    .code(500)
                    .message(e.getMessage())
                    .data(null)
                    .build();
        }
    }
}