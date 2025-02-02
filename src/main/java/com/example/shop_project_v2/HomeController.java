package com.example.shop_project_v2;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.shop_project_v2.product.repository.ProductRepository;
import com.example.shop_project_v2.member.Membership;
import com.example.shop_project_v2.member.service.MemberService;
import com.example.shop_project_v2.product.dto.ProductResponseDTO;
import com.example.shop_project_v2.product.entity.Product;
import com.example.shop_project_v2.product.entity.ProductImage;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class HomeController {
	private final ProductRepository productRepository;
	private final MemberService memberService;
	
    @GetMapping("/")
    public String home(Model model) {
        Membership membership = null;
        try {
            membership = memberService.getCurrentMember().getMembership();
        } catch (RuntimeException ex) {

        }
    	List<Product> bestSellers = productRepository.findTop5ByOrderBySalesCountDesc();
        // Product 엔티티를 ProductResponseDTO로 변환
        List<ProductResponseDTO> bestSellerDtos = bestSellers.stream()
            .map(product -> new ProductResponseDTO(
                product.getProductId(),
                product.getName(),
                product.getPrice(),
                product.getThumbnailUrl() // 썸네일 URL은 Product 엔티티에서 가져옴
            ))
            .collect(Collectors.toList());

        model.addAttribute("bestSellers", bestSellerDtos);
        model.addAttribute("membership", membership);
        return "home"; 
    }
}
