package com.example.shop_project_v2.config;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.example.shop_project_v2.member.service.MemberService;
import com.example.shop_project_v2.product.entity.Product;
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
    public boolean preHandle(HttpServletRequest req,
                             HttpServletResponse res,
                             Object handler) throws Exception {
        String membership = "";
        try {
            membership = memberService.getCurrentMember().getMembership().getName();
        } catch (Exception ignored) {}
        Long userId = (membership.isEmpty() ? null : memberService.getCurrentMember().getId());
        String key = "recent:product:" + (userId != null ? userId : "guest:" + req.getSession(true).getId());

        List<Product> recents = recentProductService.getRecentProducts(key);
        req.setAttribute("recentProducts", recents);

        return true;
    }
}
