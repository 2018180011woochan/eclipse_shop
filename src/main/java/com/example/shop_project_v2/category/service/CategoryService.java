package com.example.shop_project_v2.category.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.shop_project_v2.category.entity.Category;
import com.example.shop_project_v2.category.repository.CategoryRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class CategoryService {
    private final CategoryRepository categoryRepository;

    // 메인 카테고리 생성
    public void createMainCategory(String name) {
        Category mainCategory = new Category();
        mainCategory.setName(name);
        mainCategory.setParent(null);
        categoryRepository.save(mainCategory);
    }

    // 메인 카테고리 목록 조회
    public List<Category> getMainCategories() {
        return categoryRepository.findByParentIsNull();
    }
    
    // 서브카테고리 생성
    public void createSubCategory(Long parentId, String name) {
        Category parentCategory = categoryRepository.findById(parentId)
                .orElseThrow(() -> new IllegalArgumentException("메인 카테고리가 존재하지 않습니다."));

        Category subCategory = new Category();
        subCategory.setName(name);
        subCategory.setParent(parentCategory);

        categoryRepository.save(subCategory);
    }
}
