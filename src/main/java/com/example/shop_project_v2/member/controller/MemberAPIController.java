package com.example.shop_project_v2.member.controller;

import java.security.Principal;
import java.util.Collections;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.shop_project_v2.jwt.AuthTokenImpl;
import com.example.shop_project_v2.jwt.JwtProvider;
import com.example.shop_project_v2.jwt.JwtProviderImpl;
import com.example.shop_project_v2.member.dto.MemberResponseDTO;
import com.example.shop_project_v2.member.entity.Member;
import com.example.shop_project_v2.member.repository.MemberRepository;
import com.example.shop_project_v2.member.service.MemberService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/members")
public class MemberAPIController {
	private final MemberRepository memberRepository;
	private final MemberService memberService;
	private final PasswordEncoder  passwordEncoder;
	private final JwtProviderImpl jwtProvider;
	
	@GetMapping("/check-email")
	public ResponseEntity<Map<String, Boolean>> checkEmail(@RequestParam("email") String email) {
		boolean isExists = memberRepository.existsByEmail(email);
        Map<String, Boolean> response = Collections.singletonMap("exists", isExists);

        // 이메일 중복 시 409 Conflict 반환, 그렇지 않으면 200 OK
        if (isExists) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        } else {
            return ResponseEntity.ok(response);
        }
	}
	
	@GetMapping("/check-nickname")
	public ResponseEntity<Map<String, Boolean>> checkNickname(
            @RequestParam("nickname") String nickname,
            @RequestParam(value = "currentNickname", required = false) String currentNickname) {
        
        // 현재 닉네임과 동일하면 중복으로 간주하지 않음
        boolean isExists = (currentNickname == null || !nickname.equals(currentNickname)) 
                			&& memberRepository.existsByNickname(nickname);
        Map<String, Boolean> response = Collections.singletonMap("exists", isExists);

        // 닉네임 중복 시 409 Conflict 반환, 그렇지 않으면 200 OK
        if (isExists) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        } else {
            return ResponseEntity.ok(response);
        }
    }
	
	@PostMapping("/verify-password")
	@ResponseBody
	public ResponseEntity<?> verifyPassword(@RequestBody Map<String, String> request, Principal principal) {
	    String inputPassword = request.get("password");
	    String userEmail = principal.getName(); // 현재 로그인한 사용자의 이메일

	    Member member = memberService.findByEmail(userEmail);

	    if (passwordEncoder.matches(inputPassword, member.getPassword())) {
	        return ResponseEntity.ok().build(); // 비밀번호 일치
	    } else {
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); // 비밀번호 불일치
	    }
	}
	
    @GetMapping("/me")
    public ResponseEntity<?> getMyInfo(HttpServletRequest request) {
        try {
            // 1) 쿠키에서 accessToken 추출
            String accessToken = extractAccessToken(request);
            if (accessToken == null || accessToken.isEmpty()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("로그인이 필요합니다.(No token)");
            }


            AuthTokenImpl authToken = jwtProvider.convertAuthToken(accessToken);
            Authentication authentication = jwtProvider.getAuthentication(authToken);

            User user = (User) authentication.getPrincipal(); 
            String userId = user.getUsername(); 

            Member member = memberService.findByEmail(userId); 
            System.out.println("로그인정보: " + member.getEmail());
            if (member == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("사용자를 찾을 수 없음");
            }

            MemberResponseDTO dto = MemberResponseDTO.builder()
                    .id(member.getId())
                    .email(member.getEmail())
                    .name(member.getName())
                    .phone(member.getPhone())
                    .address(member.getAddress())       
                    .addressDetail(member.getAddressDetail())
                    .membership(member.getMembership().name())
                    .build();

            return ResponseEntity.ok(dto);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("인증 실패: " + e.getMessage());
        }
    }

    private String extractAccessToken(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie c : cookies) {
                if ("accessToken".equals(c.getName())) {
                    return c.getValue();
                }
            }
        }
        return null; 
    }
}
