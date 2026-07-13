package com.example.E_Commerce.Service;



import com.example.E_Commerce.Request.CategoryRequest;
import com.example.E_Commerce.Response.BaseApiResponse;
import com.example.E_Commerce.Response.CategoryResponse;

import java.util.List;

public interface CategoryService {

    BaseApiResponse<CategoryResponse> createCategory(CategoryRequest categoryRequest);

    BaseApiResponse<List<CategoryResponse>> getAllCategories();

    BaseApiResponse<CategoryResponse> getCategoryById(Long categoryId);

}