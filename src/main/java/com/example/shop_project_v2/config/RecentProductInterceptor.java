package com.example.shop_project_v2.config;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.example.shop_project_v2.member.service.MemberService;
import com.example.shop_project_v2.product.service.RecentProductService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.List;

@Component
@RequiredArgsConstructor
public class RecentProductInterceptor implements HandlerInterceptor {

    private final RecentProductService recentProductService;
    private final MemberService memberService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {

        String membershipName = "";
        try {
            membershipName = memberService.getCurrentMember().getMembership().getName();
        } catch (RuntimeException ex) {
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

        request.setAttribute("recentIds", recentIds);

        return true; 
    }
}
