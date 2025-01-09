package com.example.shop_project_v2.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
	
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // /uploads/** 로 들어오는 요청을 C:/uploads/ 디렉토리의 파일로 매핑
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:///C:/uploads/");
	}
}
