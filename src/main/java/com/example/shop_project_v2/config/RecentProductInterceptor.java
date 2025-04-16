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

        // 로그인 상태 체크 (예외 처리나 try-catch로 간단히 처리)
        String membershipName = "";
        try {
            membershipName = memberService.getCurrentMember().getMembership().getName();
        } catch (RuntimeException ex) {
            // 로그인 안 된 경우 그냥 빈 문자열
        }

        String redisKey;
        if (!membershipName.isEmpty()) {
            Long userId = memberService.getCurrentMember().getId();
            redisKey = "recent:product:" + userId;
        } else {
            String sessionId = request.getSession(true).getId();
            redisKey = "recent:product:guest:" + sessionId;
        }

        // 인터셉터 단계에서 Redis에 저장된 최근 본 상품 ID 가져오기
        List<Long> recentIds = recentProductService.getRecentProductIds(redisKey);

        // 이제 이 데이터를 request에 넣어서, 뷰에서 꺼내 쓸 수 있게 한다.
        request.setAttribute("recentIds", recentIds);

        return true; 
    }
}
