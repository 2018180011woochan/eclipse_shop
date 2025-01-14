package com.example.shop_project_v2.product.dto;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductRequestDTO {
	private Long productId;
	private String name;
	
	private int price;
	
	private String description;
	
	private Long categoryId;
	
	private List<MultipartFile> images;
	
    @NotEmpty(message = "옵션은 최소 1개 항목 이상 선택해야 합니다.")
    private List<ProductOptionRequestDTO> options;
}
