package com.example.shop_project_v2.admin.category;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.shop_project_v2.category.dto.CategoryResponseDTO;
import com.example.shop_project_v2.category.entity.Category;
import com.example.shop_project_v2.category.service.CategoryService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/category")
@RequiredArgsConstructor
public class adminCategoryAPIController {
    private final CategoryService categoryService;
    
    @GetMapping("/categoryList")
    public List<CategoryResponseDTO> getAllCategories() {
        List<Category> mainCategories = categoryService.getMainCategories();
        return mainCategories.stream().map(CategoryResponseDTO::fromEntity).toList();
    }

    @ResponseBody
    @GetMapping("/{mainCategoryId}/subcategorys")
    public List<Category> getSubCategories(@PathVariable Long mainCategoryId) {
        return categoryService.findSubCategoriesByMainCategoryId(mainCategoryId);
    }
}
