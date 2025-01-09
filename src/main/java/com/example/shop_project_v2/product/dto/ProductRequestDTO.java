package com.example.shop_project_v2.product.dto;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductRequestDTO {
	private String name;
	
	private int price;
	
	private String description;
	
	private List<MultipartFile> images;
}
