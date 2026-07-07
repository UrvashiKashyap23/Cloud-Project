package com.example.E_Commerce.Controller;

import com.example.E_Commerce.DTO.CategoryDto;
import com.example.E_Commerce.Service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/v1/category")
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping("/create")
    public CategoryDto createCategory (@RequestBody CategoryDto categoryDto){
        return categoryService.createCategory(categoryDto);
    }
    @GetMapping("/{id}")
    public CategoryDto getCategory(@PathVariable Long id){
        return categoryService.getCategoryById(id);
    }

    @GetMapping("/getAll")
    public List<CategoryDto> getAllCategories() {
        return categoryService.getAllCategories();
    }
}
