package com.example.shop_project_v2.admin.category;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.shop_project_v2.category.service.CategoryService;

import lombok.RequiredArgsConstructor;

@RequestMapping("/admin/categorys")
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
        return "redirect:/admin/categorys";
    }
}
