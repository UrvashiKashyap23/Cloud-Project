package com.example.E_Commerce.Controller;

import com.example.E_Commerce.Request.CategoryRequest;
import com.example.E_Commerce.Response.BaseApiResponse;
import com.example.E_Commerce.Response.CategoryResponse;
import com.example.E_Commerce.Service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/category")
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping("/create")
    public ResponseEntity<BaseApiResponse<CategoryResponse>> createCategory(@RequestBody CategoryRequest categoryRequest) {

        return ResponseEntity.ok(categoryService.createCategory(categoryRequest));
    }

    @GetMapping("/{categoryId}")
    public ResponseEntity<BaseApiResponse<CategoryResponse>> getCategoryById(
            @PathVariable Long categoryId) {

        return ResponseEntity.ok(categoryService.getCategoryById(categoryId));
    }

    @GetMapping("/getAll")
    public ResponseEntity<BaseApiResponse<List<CategoryResponse>>> getAllCategories() {

        return ResponseEntity.ok(categoryService.getAllCategories());
    }
}
