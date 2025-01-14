package com.example.shop_project_v2.category.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.shop_project_v2.category.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    // 상위 카테고리가 없는 메인 카테고리 조회
    List<Category> findByParentIsNull();
}