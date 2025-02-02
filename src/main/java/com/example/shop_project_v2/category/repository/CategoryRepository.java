package com.example.shop_project_v2.category.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.shop_project_v2.category.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findByParentIsNull();
    List<Category> findByParent_CategoryId(Long parentId);
}