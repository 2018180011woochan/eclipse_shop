package com.example.shop_project_v2.admin.product;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/admin/products")
@Controller
public class adminProductViewController {
	@GetMapping
	public String ProductControllList() {
		return "admin/product/adminProductList";
	}
	
	@GetMapping("/create")
	public String CreateProduct() {
		return "admin/product/ProductCreatePage";
	}
}
