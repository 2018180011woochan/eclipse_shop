package com.example.shop_project_v2.jwt;


public interface AuthToken <T> {
	String AUTHORITIES_TOKEN_KEY = MemberConstants.AUTHORIZATION_TOKEN_KEY;
	
	boolean validate();
	
	T getDate();

}
