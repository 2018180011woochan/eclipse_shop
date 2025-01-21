package com.example.shop_project_v2.jwt.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class JwtTokenDto {
	private String accessToken;
	private String refreshToken;
}
