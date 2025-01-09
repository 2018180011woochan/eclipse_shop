package com.example.shop_project_v2.product.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.shop_project_v2.product.entity.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
	public Product save(Product product);
}
