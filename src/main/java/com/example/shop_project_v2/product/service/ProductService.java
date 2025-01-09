package com.example.shop_project_v2.product.service;

import org.springframework.stereotype.Service;

import com.example.shop_project_v2.product.dto.ProductRequestDTO;
import com.example.shop_project_v2.product.entity.Product;
import com.example.shop_project_v2.product.repository.ProductRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ProductService {
	private final ProductRepository productRepository;
	
	public void CreateProduct(ProductRequestDTO productRequestDto) {
		System.out.println("DTO 값 확인: " + productRequestDto.getName());
		Product product = Product.builder()
				.name(productRequestDto.getName())
				.price(productRequestDto.getPrice())
				.description(productRequestDto.getDescription())
				.build();
		System.out.println("Entity 값 확인: " + product);
		productRepository.save(product);
	}
}
