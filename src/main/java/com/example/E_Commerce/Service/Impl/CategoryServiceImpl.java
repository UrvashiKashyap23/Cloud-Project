package com.example.E_Commerce.Service.Impl;

import com.example.E_Commerce.DTO.CategoryDto;
import com.example.E_Commerce.Entity.Category;
import com.example.E_Commerce.Repository.CategoryRepository;
import com.example.E_Commerce.Service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    //Service Layer receives DTO and internally works with entity and return DTO

    private final ModelMapper modelMapper;

    @Override
    public CategoryDto createCategory(CategoryDto categoryDto) {
        Category category = modelMapper.map(categoryDto, Category.class); //DTO->Entity
        Category savedCategory = categoryRepository.save(category);
        return modelMapper.map(savedCategory, CategoryDto.class);
    }

    @Override
    public List<CategoryDto> getAllCategories() {
        List<Category> categories=categoryRepository.findAll();
       // single element can be converted by modelmapper but for multiple projects , we need stream.
        return categories.stream().map((category) -> modelMapper.map(category, CategoryDto.class)).toList();
    }

    @Override
    public CategoryDto getCategoryById(Long id) {
        Category category= categoryRepository.findById(id).orElseThrow(()->new RuntimeException("No element found with the id: "+ id));
        return modelMapper.map(category,CategoryDto.class);
    }


}
