package com.example.shop_project_v2.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.example.shop_project_v2.config.RecentProductInterceptor;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {
	
	private final RecentProductInterceptor recentProductInterceptor;
	
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 모든 경로에 대해 인터셉터 적용 (꼭 필요한 경로에만 적용하라고 말도 안 해도, 일단 전부 적용하자)
        registry.addInterceptor(recentProductInterceptor)
                .addPathPatterns("/**");
    }
	
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // /uploads/** 로 들어오는 요청을 C:/uploads/ 디렉토리의 파일로 매핑
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:///C:/uploads/");
	}
}
