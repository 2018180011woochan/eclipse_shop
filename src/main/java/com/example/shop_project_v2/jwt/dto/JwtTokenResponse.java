package com.example.shop_project_v2.jwt.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Builder
@ToString
@Getter
public class JwtTokenResponse {
	private String accessToken;

}
