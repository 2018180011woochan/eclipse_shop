package com.example.shop_project_v2.product.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.shop_project_v2.product.entity.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
	List<Product> findAllByOrderByCreatedDateDesc(); // 최신순
	List<Product> findAllByOrderByCreatedDateAsc();  // 등록순
}
