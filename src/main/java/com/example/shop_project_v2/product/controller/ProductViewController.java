package com.example.shop_project_v2.product.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.shop_project_v2.member.Membership;
import com.example.shop_project_v2.member.entity.Member;
import com.example.shop_project_v2.member.service.MemberService;
import com.example.shop_project_v2.product.entity.Product;
import com.example.shop_project_v2.product.service.ProductService;

import lombok.RequiredArgsConstructor;
@RequiredArgsConstructor
@Controller
@RequestMapping("/products")
public class ProductViewController {
	private final ProductService productService;
	private final MemberService memberService;
	
	@GetMapping("/productList")
    public String viewProductsByCategory(
            @RequestParam(required = false) Long categoryId,
            @RequestParam(defaultValue = "newest") String sort,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "8") int size,
            Model model
    ) {
        Page<Product> productPage = productService.getProductsByCategory(categoryId, sort, page, size);
        Membership membership = memberService.getCurrentMember().getMembership();
        
        model.addAttribute("products", productPage.getContent());
        model.addAttribute("currentPage", productPage.getNumber());
        model.addAttribute("totalPages", productPage.getTotalPages());
        model.addAttribute("sort", sort);
        model.addAttribute("size", size);
        model.addAttribute("categoryId", categoryId);
        model.addAttribute("membership", membership);

        return "product/productList"; 
    }
	
	@GetMapping("/{id}")
	public String ViewProductDetail(@PathVariable Long id, Model model) {
        Product product = productService.FindById(id);
        String membership = memberService.getCurrentMember().getMembership().getName();
        
        int originalPrice = product.getPrice();
        int discountPrice = originalPrice;
        
        if (membership.equals("GOLD")) {
            discountPrice = (int) Math.round((originalPrice * 0.95) / 10.0) * 10;
        } else if (membership.equals("DIAMOND")) {
            discountPrice = (int) Math.round((originalPrice * 0.90) / 10.0) * 10;
        }
        
        model.addAttribute("product", product);
        model.addAttribute("membership", membership);
        model.addAttribute("discountPrice", discountPrice);
		return "product/productDetail";
	}
	
}
