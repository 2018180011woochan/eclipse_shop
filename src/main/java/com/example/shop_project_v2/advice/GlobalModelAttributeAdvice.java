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
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
            // 로그인 안 된 경우 그냥 무시
        }

        String redisKey;
        if (!membershipName.isEmpty()) {
            Long userId = memberService.getCurrentMember().getId();
            redisKey = "recent:product:" + userId;
        } else {
            String sessionId = request.getSession(true).getId();
            redisKey = "recent:product:guest:" + sessionId;
        }

        List<Long> recentIds = recentProductService.getRecentProductIds(redisKey);
        if (recentIds == null || recentIds.isEmpty()) {
            return new ArrayList<>();
        }
        
        List<Product> products = productService.findProductsByIds(recentIds);
        

        Map<Long, Integer> orderMap = new HashMap<>();
        for (int i = 0; i < recentIds.size(); i++) {
            orderMap.put(recentIds.get(i), i);
        }
        
        products.sort(Comparator.comparingInt(p -> orderMap.get(p.getProductId())));
        
        return products;
    }
}
