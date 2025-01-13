package com.example.shop_project_v2.product.controller;

import java.util.List;

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
	
	@GetMapping()	// 임시로, 나중에 카테고리 만들어서 넣을거임
	public String ViewProducts(
			@RequestParam(defaultValue = "newest") String sort,
			Model model) {
		List<Product> products = productService.getAllProducts();

		model.addAttribute("products" , products);
		return "product/productList";
	}
	
	@GetMapping("/{id}")
	public String ViewProductDetail(@PathVariable Long id, Model model) {
        Product product = productService.FindById(id);
        model.addAttribute("product", product);
		return "product/productDetail";
	}
	
}
