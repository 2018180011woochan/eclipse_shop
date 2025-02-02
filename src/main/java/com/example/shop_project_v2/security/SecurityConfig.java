package com.example.shop_project_v2.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.example.shop_project_v2.jwt.JwtFilter;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
	private final JwtFilter jwtFilter;
//	private final CustomOAuth2UserService customOAuth2UserService;
//	private final CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;
	private final CustomLogoutSuccessHandler customLogoutSuccessHandler;
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                        		"/login", 
                                "/join", 
                                "/jwt-login", 
                                "/css/**", 
                                "/js/**", 
                                "/images/**", 
                                "/order/**", 
                                "/member/**",
                                "/cart/**",
                                "/inquiry/**",
                                "/",
                                "/api/**",
                                "/oauth2/**",
                                "/signup/**",
								"/products/**",
                                "/signup/**",
                                "/chatbot/**",
								"/common/**",
								"/password-reset",
								"/mainpage/**",
								"/product/**",
								"/upload/**",
								"/uploads/**",
								"/review/**",
								"/chatbot/js/**",
								"/chatbot.html"
                        ).permitAll()
                        .requestMatchers("/mypage").authenticated()
                        .requestMatchers("/admin/**").hasAuthority("ROLE_ADMIN")
                        .anyRequest().authenticated()
                )
                .csrf(AbstractHttpConfigurer::disable)
        		.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class) // JWT 필터 추가
//        		.oauth2Login(oauth2 -> oauth2
//        			    .loginPage("/login")
//        			    //.defaultSuccessUrl("/signup/confirm") 
//        			    .successHandler(customAuthenticationSuccessHandler)
//        			    .failureUrl("/login?error=true")
//        			    .userInfoEndpoint(userInfo -> userInfo.userService(customOAuth2UserService))
//        			)
		        .logout(logout -> logout
		                .logoutUrl("/logout")
		                .logoutSuccessHandler(customLogoutSuccessHandler) 
		                .deleteCookies("accessToken", "refreshToken", "JSESSIONID") // 관련 쿠키 삭제
		                .invalidateHttpSession(true)
		                .permitAll()
		          );
        
        return http.build();
    }
}
