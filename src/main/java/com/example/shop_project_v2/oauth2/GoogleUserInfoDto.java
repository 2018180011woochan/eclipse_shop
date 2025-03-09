package com.example.shop_project_v2.oauth2;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GoogleUserInfoDto {
	private String email;
	private String name;
	private String providerId;
}
