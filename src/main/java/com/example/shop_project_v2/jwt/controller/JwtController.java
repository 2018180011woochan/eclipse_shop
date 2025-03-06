package com.example.shop_project_v2.jwt.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.shop_project_v2.jwt.JwtProvider;
import com.example.shop_project_v2.jwt.dto.JwtTokenDto;
import com.example.shop_project_v2.jwt.dto.JwtTokenLoginRequest;
import com.example.shop_project_v2.jwt.dto.JwtTokenResponse;
import com.example.shop_project_v2.member.service.MemberService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequiredArgsConstructor
public class JwtController {
	private final MemberService memberService;
	private final JwtProvider jwtProvider;
	
	@PostMapping("/jwt-login")
	public ResponseEntity<JwtTokenResponse> jwtLogin(
	        @RequestBody JwtTokenLoginRequest request,
	        HttpServletResponse response) {

	    JwtTokenDto jwtTokenDto = memberService.login(request);

	    String accessTokenCookie = String.format(
	        "accessToken=%s; Path=/; HttpOnly; SameSite=Lax; Max-Age=%d",
	        jwtTokenDto.getAccessToken(),
	        10 * 60  // 10분
	    );
	    String refreshTokenCookie = String.format(
	        "refreshToken=%s; Path=/; HttpOnly; SameSite=Lax; Max-Age=%d",
	        jwtTokenDto.getRefreshToken(),
	        14 * 24 * 60 * 60  // 14일
	    );

	    response.addHeader("Set-Cookie", accessTokenCookie);
	    response.addHeader("Set-Cookie", refreshTokenCookie);

	    return ResponseEntity.ok(
	        JwtTokenResponse.builder()
	                        .accessToken(jwtTokenDto.getAccessToken())
	                        .build()
	    );
	}

}
