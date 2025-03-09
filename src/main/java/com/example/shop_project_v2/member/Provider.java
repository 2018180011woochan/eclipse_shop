package com.example.shop_project_v2.member;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Provider {
	GOOGLE("GOOGLE"),
	NONE("NONE");
	
	private final String value;
}
