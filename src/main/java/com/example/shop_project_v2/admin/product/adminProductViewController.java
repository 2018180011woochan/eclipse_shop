package com.example.shop_project_v2.admin.product;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.shop_project_v2.product.dto.ProductRequestDTO;
import com.example.shop_project_v2.product.entity.Product;
import com.example.shop_project_v2.product.service.ProductService;

import lombok.RequiredArgsConstructor;

@RequestMapping("/admin/products")
@RequiredArgsConstructor
@Controller
public class adminProductViewController {
	private final ProductService productService;
	
	@GetMapping
	public String ProductControllList(Model model) {
		List<Product> products = productService.getAllProducts();
		model.addAttribute("products" , products);
		return "admin/product/adminProductList";
	}
	
	@GetMapping("/create")
	public String CreateProductPage() {
		return "admin/product/ProductCreatePage";
	}
	
	@PostMapping("/create")
	public String CreateProduct(@ModelAttribute ProductRequestDTO productRequestDto) {
		productService.CreateProduct(productRequestDto);
		return "redirect:/admin/products";
	}
}
