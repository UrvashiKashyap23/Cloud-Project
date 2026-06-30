package com.example.E_Commerce.Service;

import com.example.E_Commerce.DTO.CategoryDto;

import java.util.List;

public interface CategoryService {  //define ,methods

    CategoryDto createCategory(CategoryDto categoryDto);

    List<CategoryDto> getAllCategories();

    CategoryDto getCategoryById(Long id);





}
