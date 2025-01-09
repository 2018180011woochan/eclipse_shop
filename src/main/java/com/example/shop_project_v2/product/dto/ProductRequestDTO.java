package com.example.shop_project_v2.product.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductRequestDTO {
	private String name;
	
	private int price;
	
	private String description;
}
