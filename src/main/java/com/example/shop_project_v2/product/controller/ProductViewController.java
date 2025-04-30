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
import com.example.shop_project_v2.product.service.RecentProductService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
@RequestMapping("/products")
public class ProductViewController {
	private final ProductService productService;
	private final MemberService memberService;
	private final RecentProductService recentProductService;
	
	@GetMapping("/productList")
    public String viewProductsByCategory(
            @RequestParam(required = false) Long categoryId,
            @RequestParam(defaultValue = "newest") String sort,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "8") int size,
            @RequestParam(required = false) String keyword,
            Model model
    ) {
        Page<Product> productPage = productService.getProductsByCategory(categoryId, sort, page, size);
        
        // 로그인 여부 확인
        Membership membership = null;
        try {
            membership = memberService.getCurrentMember().getMembership();
        } catch (RuntimeException ex) {
            // 로그인되지 않은 경우 예외 발생 -> membership은 null 유지
        }
        
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
	public String ViewProductDetail(@PathVariable Long id, Model model, HttpServletRequest request) {
        Product product = productService.FindById(id);
        String membershipName = "";
        int originalPrice = product.getPrice();
        int discountPrice = originalPrice;
        
        try {
            membershipName = memberService.getCurrentMember().getMembership().getName();
        } catch (RuntimeException ex) {
            membershipName = ""; // 비로그인 상태 -> 할인 없음
        }
        if (membershipName.equals("GOLD")) {
            discountPrice = (int) Math.round((originalPrice * 0.95) / 10.0) * 10;
        } else if (membershipName.equals("DIAMOND")) {
            discountPrice = (int) Math.round((originalPrice * 0.90) / 10.0) * 10;
        }
        
        model.addAttribute("product", product);
        model.addAttribute("membership", membershipName);
        model.addAttribute("discountPrice", discountPrice);
        
        // 최근 본 상품 저장
        String redisKey;
        if (!membershipName.isEmpty()) {
            Long userId = memberService.getCurrentMember().getId();
            redisKey = "recent:product:" + userId;
        } else {
            // 비로그인: 세션 ID 사용
            String sessionId = request.getSession(true).getId();
            redisKey = "recent:product:guest:" + sessionId;
        }
        
        // Redis에 최근 본 상품 기록
        recentProductService.saveRecentProduct(redisKey, id);
        
        List<Product> recentProducts = recentProductService.getRecentProducts(redisKey);
        model.addAttribute("recentProducts", recentProducts);
        
		return "product/productDetail";
	}
	
}
