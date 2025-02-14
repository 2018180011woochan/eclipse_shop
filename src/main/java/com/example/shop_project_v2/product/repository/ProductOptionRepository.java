package com.example.shop_project_v2.product.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.shop_project_v2.product.Size;
import com.example.shop_project_v2.product.entity.ProductOption;

@Repository
public interface ProductOptionRepository extends JpaRepository<ProductOption, Long> {
	Optional<ProductOption> findByProduct_ProductIdAndColorAndSize(Long productId, String color, Size size);
}
