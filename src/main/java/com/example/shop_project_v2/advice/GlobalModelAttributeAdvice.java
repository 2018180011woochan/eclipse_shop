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

    @ModelAttribute("recentProducts")
    public List<Product> populateRecentProducts(HttpServletRequest request) {
        String redisKey;
        try {
            String membership = memberService.getCurrentMember().getMembership().getName();
            Long userId = memberService.getCurrentMember().getId();
            redisKey = "recent:product:" + userId;
        } catch (RuntimeException ex) {
            // 비로그인
            String sessionId = request.getSession(true).getId();
            redisKey = "recent:product:guest:" + sessionId;
        }

        return recentProductService.getRecentProducts(redisKey);
    }
}
