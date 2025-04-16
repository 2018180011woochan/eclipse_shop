package com.example.shop_project_v2.advice;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.example.shop_project_v2.member.service.MemberService;
import com.example.shop_project_v2.product.entity.Product;
import com.example.shop_project_v2.product.service.ProductService;
import com.example.shop_project_v2.product.service.RecentProductService;

import jakarta.servlet.http.HttpServletRequest;

import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
@RequiredArgsConstructor
public class GlobalModelAttributeAdvice {

    private final RecentProductService recentProductService;
    private final MemberService memberService;
    private final ProductService productService;  

    @ModelAttribute("recentProducts")
    public List<Product> populateRecentProducts(HttpServletRequest request) {
        String membershipName = "";
        try {
            membershipName = memberService.getCurrentMember().getMembership().getName();
        } catch (RuntimeException ex) {
            // 로그인 안된 거라면 그냥 무시
        }

        String redisKey;
        if (!membershipName.isEmpty()) {
            Long userId = memberService.getCurrentMember().getId();
            redisKey = "recent:product:" + userId;
        } else {
            String sessionId = request.getSession(true).getId();
            redisKey = "recent:product:guest:" + sessionId;
        }

        // 먼저 Redis에서 최근 본 상품 ID 리스트 가져오기 (예: List<Long>)
        List<Long> recentIds = recentProductService.getRecentProductIds(redisKey);
        if(recentIds == null || recentIds.isEmpty()){
            return new ArrayList<>(); // 빈 리스트 반환
        }
        // 이 상품 ID들을 이용해서 실제 Product 객체 리스트로 변환해주자.
        return productService.findProductsByIds(recentIds);
    }
}
