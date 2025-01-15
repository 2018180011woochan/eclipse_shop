package com.example.shop_project_v2.category.dto;

import java.util.List;

import com.example.shop_project_v2.category.entity.Category;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoryResponseDTO {
    private Long categoryId;
    private String categoryName;
    private List<CategoryResponseDTO> subCategories;

    public static CategoryResponseDTO fromEntity(Category category) {
        CategoryResponseDTO dto = new CategoryResponseDTO();
        dto.setCategoryId(category.getCategoryId());
        dto.setCategoryName(category.getName());
        dto.setSubCategories(
            category.getSubCategories().stream().map(CategoryResponseDTO::fromEntity).toList()
        );
        return dto;
    }
}
