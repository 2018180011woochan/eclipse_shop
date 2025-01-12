package com.example.shop_project_v2.product.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.shop_project_v2.product.entity.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
	Page<Product> findAllByOrderByCreatedDateDesc(Pageable pageable); // 최신순
	Page<Product> findAllByOrderByCreatedDateAsc(Pageable pageable);  // 등록순
	
	Page<Product> findByNameContaining(String keyword, Pageable pageable); // 상품 검색
}
