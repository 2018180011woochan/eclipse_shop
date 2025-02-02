package com.example.shop_project_v2.admin.product;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.shop_project_v2.category.entity.Category;
import com.example.shop_project_v2.category.service.CategoryService;
import com.example.shop_project_v2.product.dto.ProductRequestDTO;
import com.example.shop_project_v2.product.entity.Product;
import com.example.shop_project_v2.product.service.ProductService;

import lombok.RequiredArgsConstructor;

@RequestMapping("/admin/products")
@RequiredArgsConstructor
@Controller
public class AdminProductViewController {
	private final ProductService productService;
	private final CategoryService categoryService;
	@GetMapping
	public String ProductControllList(Model model) {
		List<Product> products = productService.getAllProducts();
		model.addAttribute("products" , products);
		return "admin/product/adminProductList";
	}
	
	@GetMapping("/create")
	public String CreateProductPage(Model model) {
	    List<Category> mainCategories = categoryService.getMainCategories(); 
	    model.addAttribute("mainCategories", mainCategories);
		return "admin/product/ProductCreatePage";
	}
	
	@PostMapping("/create")
	public String CreateProduct(@ModelAttribute ProductRequestDTO productRequestDto) {
		System.out.println("categoryId: " + productRequestDto.getCategoryId());
		productService.CreateProduct(productRequestDto);
		return "redirect:/admin/products";
	}
	
	@GetMapping("/edit/{id}")
	public String EditProduct(@PathVariable Long id, Model model) {
		Product product = productService.FindById(id);

	    ProductRequestDTO dto = new ProductRequestDTO();
	    dto.setProductId(product.getProductId());
	    dto.setName(product.getName());
	    dto.setPrice(product.getPrice());
	    dto.setDescription(product.getDescription());

	    model.addAttribute("productRequestDTO", dto);
		
		return "admin/product/ProductEditPage";
	}
	
	@PostMapping("/edit/{id}")
	public String EditProduct(@PathVariable Long id, @ModelAttribute ProductRequestDTO productRequestDto) {
	    productService.UpdateProduct(id, productRequestDto);
	    return "redirect:/admin/products";
	}
	
	@PostMapping("/delete/{id}")
	public String DeleteProduct(@PathVariable Long id) {
	    productService.DeleteProduct(id);
	    return "redirect:/admin/products";
	}
}
