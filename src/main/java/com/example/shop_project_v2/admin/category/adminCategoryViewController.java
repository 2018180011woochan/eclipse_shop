package com.example.shop_project_v2.admin.category;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.shop_project_v2.category.dto.CategoryResponseDTO;
import com.example.shop_project_v2.category.entity.Category;
import com.example.shop_project_v2.category.service.CategoryService;

import lombok.RequiredArgsConstructor;

@RequestMapping("/admin/category")
@RequiredArgsConstructor
@Controller
public class adminCategoryViewController {
	private final CategoryService categoryService;

    // 카테고리 관리 페이지
    @GetMapping()
    public String categoryPage(Model model) {
        // 메인 카테고리 목록 조회
        model.addAttribute("categories", categoryService.getMainCategories());
        return "admin/category/categoryPage";
    }

    // 메인 카테고리 생성
    @PostMapping()
    public String createMainCategory(@RequestParam String name) {
        categoryService.createMainCategory(name);
        return "redirect:/admin/category";
    }
    
    @PostMapping("/sub")
    public String createSubCategory(@RequestParam Long parentId, @RequestParam String name) {
        categoryService.createSubCategory(parentId, name);
        return "redirect:/admin/category";
    }

    
    @PostMapping("/update")
    public String updateCategory(@RequestParam Long categoryId, @RequestParam String name) {
        categoryService.updateCategory(categoryId, name);
        return "redirect:/admin/category";
    }

    @PostMapping("/delete")
    public String deleteCategory(@RequestParam Long categoryId) {
        categoryService.deleteCategory(categoryId);
        return "redirect:/admin/category";
    }

}
