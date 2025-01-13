package com.example.shop_project_v2.product.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ProductResponseDTO {
	private Long productId;
    private String name;
    private int price;
    private String thumbnailUrl;
}
