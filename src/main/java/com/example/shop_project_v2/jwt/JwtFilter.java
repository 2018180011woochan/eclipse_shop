package com.example.shop_project_v2.jwt;

import java.io.IOException;
import java.util.Optional;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtFilter extends OncePerRequestFilter {

    private final JwtProviderImpl tokenProvider;

    @Override
    protected void doFilterInternal(
        HttpServletRequest request,
        HttpServletResponse response,
        FilterChain filterChain
    ) throws ServletException, IOException {
    	String path = request.getRequestURI().substring(request.getContextPath().length());
    	log.info("JwtFilter - request path: {}", path);
    	
    	// /chatbot 경로는 JWT 검증을 건너뜀
        if (path.equals("/chatbot")) {
        	log.info("/chatbot detected. Skipping JWT check.");
            filterChain.doFilter(request, response);
            return;
        }
        
        if (path.startsWith("/chatbot/chatbot.html") || path.startsWith("/chatbot/js/")) {
        	filterChain.doFilter(request, response);
            return;
        }
    	

        Optional<String> accessToken = getAccessTokenFromCookie(request);
        log.info("AccessToken from cookie: {}", accessToken.orElse("EMPTY"));
        

        if (accessToken.isPresent()) {
            AuthTokenImpl jwtToken = tokenProvider.convertAuthToken(accessToken.get());

            if (jwtToken.validate()) {
                try {
                    Authentication authentication = tokenProvider.getAuthentication(jwtToken);
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                } catch (Exception e) {
                    // 인증 중 발생한 예외 처리
                    SecurityContextHolder.clearContext();
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    return;
                }
            } else {
                // 토큰이 유효하지 않을 경우
                SecurityContextHolder.clearContext();
            }
        }

        filterChain.doFilter(request, response);
    }
    
    private Optional<String> getAccessTokenFromCookie(HttpServletRequest request) {
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("accessToken".equals(cookie.getName())) {
                    return Optional.of(cookie.getValue());
                }
            }
        }
        return Optional.empty();
    }
}
