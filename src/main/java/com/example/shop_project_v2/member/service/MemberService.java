package com.example.shop_project_v2.member.service;

import java.util.Optional;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.shop_project_v2.coupon.service.CouponService;
import com.example.shop_project_v2.jwt.AuthTokenImpl;
import com.example.shop_project_v2.jwt.JwtProviderImpl;
import com.example.shop_project_v2.jwt.dto.JwtTokenDto;
import com.example.shop_project_v2.jwt.dto.JwtTokenLoginRequest;
import com.example.shop_project_v2.member.dto.MemberRequestDTO;
import com.example.shop_project_v2.member.entity.Member;
import com.example.shop_project_v2.member.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class MemberService {
	private final MemberRepository memberRepository;
	private final PasswordEncoder  passwordEncoder;
	private final CouponService couponService;
	
	// jwt
	private final JwtProviderImpl jwtProvider;
	
	public void Join(MemberRequestDTO memberDTO) {
		// 이메일과 닉네임 중복 검사 (비동기로 중복 검사 하지만 안정성을 위해 추가)
		checkEmailAndNicknameExist(memberDTO);
	    
	    String encryptedPassword = passwordEncoder.encode(memberDTO.getPassword());
	    
	    Member member = Member.builder()
	            .email(memberDTO.getEmail())
	            .nickname(memberDTO.getNickname())
	            .password(encryptedPassword)
	            .name(memberDTO.getName())
	            .phone(memberDTO.getPhone())
	            .postNo(memberDTO.getPostNo())
	            .address(memberDTO.getAddress())
	            .addressDetail(memberDTO.getAddressDetail())
	            .build();
        
        memberRepository.save(member);	
        
        couponService.PushCoupon(member, "회원가입 축하 쿠폰", 30);
        couponService.PushCoupon(member, member.getMembership().getName() + "등급 월간 쿠폰", 5);
	}
	
	public Member findByEmail(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
    }
	
	public void updateMember(Member member, MemberRequestDTO memberRequestDTO) {
		String encryptedPassword = passwordEncoder.encode(memberRequestDTO.getPassword());

        // 요청 받은 정보로 기존의 회원 정보를 업데이트
        member.setNickname(memberRequestDTO.getNickname());
        member.setPhone(memberRequestDTO.getPhone());
        member.setPassword(encryptedPassword);
        member.setPostNo(memberRequestDTO.getPostNo());
        member.setAddress(memberRequestDTO.getAddress());
        member.setAddressDetail(memberRequestDTO.getAddressDetail());

        memberRepository.save(member);
    }
	
	private void checkEmailAndNicknameExist(MemberRequestDTO memberDTO) {
        Optional<Member> existingMemberByEmail = memberRepository.findByEmail(memberDTO.getEmail());
        if (existingMemberByEmail.isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
        }

        Optional<Member> existingMemberByNickname = memberRepository.findByNickname(memberDTO.getNickname());
        if (existingMemberByNickname.isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 닉네임입니다.");
        }
    }
	
	// jwt
	public JwtTokenDto login(JwtTokenLoginRequest request) {
	    Member member = memberRepository.findByEmail(request.getEmail())
	        .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

	    if (!passwordEncoder.matches(request.getPassword(), member.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }
	    
	    String subject = member.getEmail();   

	    AuthTokenImpl accessToken = jwtProvider.createAccessToken(
	        subject,          
	        member.getRole(),
	        null
	    );

	    AuthTokenImpl refreshToken = jwtProvider.createRefreshToken(
	        subject,          
	        member.getRole(),
	        null
	    );

	    return JwtTokenDto.builder()
	        .accessToken(accessToken.getToken())
	        .refreshToken(refreshToken.getToken())
	        .build();
	}
//	
//	// Google OAUTH2
//	public Member saveOrUpdateGoogleUser(String email, String name, String providerId) {
//        return memberRepository.findByEmail(email)
//            .map(user -> {
//                user.setName(name);          
//                user.setProvider(Provider.GOOGLE);   
//
//                return memberRepository.save(user);
//            })
//            .orElseGet(() -> {
//                return memberRepository.save(Member.builder()
//                    .email(email)
//                    .name(name)
//                    .provider(Provider.GOOGLE)
//
//                    .role(Role.USER) 
//                    .build());
//            });
//    }
	
    public Member getCurrentMember() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() 
            || auth.getPrincipal().equals("anonymousUser")) {
            throw new RuntimeException("로그인 안 됨");
        }

        User principal = (User) auth.getPrincipal();
        String email = principal.getUsername();

        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("해당 회원이 존재하지 않습니다."));
    }
}