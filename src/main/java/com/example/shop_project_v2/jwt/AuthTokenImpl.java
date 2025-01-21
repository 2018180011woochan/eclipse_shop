package com.example.shop_project_v2.jwt;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.example.shop_project_v2.member.Role;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.impl.DefaultClaims;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Getter
@AllArgsConstructor
@Slf4j
public class AuthTokenImpl implements AuthToken<Claims> {
    private final String token;
    private final Key key;
    
    public AuthTokenImpl(String userId, Role role, Key key, Map<String, Object> claimsMap, Date expiredDate) {
    	this.key = key;
    	
    	Map<String, Object> modifiableMap = new HashMap<>();
    	if (claimsMap != null) {
    		modifiableMap.putAll(claimsMap);
    	}
    	
    	DefaultClaims claims = new DefaultClaims(modifiableMap);
    	claims.put(MemberConstants.AUTHORIZATION_TOKEN_KEY, role.getKey());
    	
    	this.token = Jwts.builder()
                .setSubject(userId)
                .addClaims(claims)
                .signWith(key, SignatureAlgorithm.HS256)
                .setExpiration(expiredDate)
                .compact();
    }
    
    @Override
    public boolean validate() {
        return getDate() != null;
    }

    @Override
    public Claims getDate() {
        try {
            return Jwts
                    .parserBuilder()
                    .setSigningKey(key) 
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (SecurityException e) {
            log.warn("Invalid JWT signature");
        } catch (MalformedJwtException e) {
            log.warn("Invalid JWT token");
        } catch (ExpiredJwtException e) {
            log.warn("Expired JWT token");
        } catch (UnsupportedJwtException e) {
            log.warn("Unsupported JWT Token");
        } catch (IllegalArgumentException e) {
            log.warn("JWT token compact of handler are invalid");
        }

        return null;
    }
    
    
    
    
    
}
