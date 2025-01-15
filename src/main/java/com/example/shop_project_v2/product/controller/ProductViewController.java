package com.example.shop_project_v2.product.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.shop_project_v2.product.entity.Product;
import com.example.shop_project_v2.product.service.ProductService;

import lombok.RequiredArgsConstructor;
@RequiredArgsConstructor
@Controller
@RequestMapping("/products")
public class ProductViewController {
	private final ProductService productService;
	
	@GetMapping("/productList")
    public String viewProductsByCategory(
            @RequestParam(required = false) Long categoryId,
            @RequestParam(defaultValue = "newest") String sort,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "8") int size,
            Model model
    ) {
        Page<Product> productPage = productService.getProductsByCategory(categoryId, sort, page, size);

        model.addAttribute("products", productPage.getContent());
        model.addAttribute("currentPage", productPage.getNumber());
        model.addAttribute("totalPages", productPage.getTotalPages());
        model.addAttribute("sort", sort);
        model.addAttribute("size", size);
        model.addAttribute("categoryId", categoryId);

        return "product/productList"; 
    }
	
	@GetMapping("/{id}")
	public String ViewProductDetail(@PathVariable Long id, Model model) {
        Product product = productService.FindById(id);
        model.addAttribute("product", product);
		return "product/productDetail";
	}
	
}
