package com.example.shop_project_v2.coupon.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.shop_project_v2.coupon.entity.Coupon;
import com.example.shop_project_v2.coupon.repository.CouponRepository;
import com.example.shop_project_v2.member.entity.Member;
import com.example.shop_project_v2.member.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CouponService {
    private final CouponRepository couponRepository;
    private final MemberRepository memberRepository;
    
    public void PushCoupon(Member member, String reason, int discountRate) {
        Coupon coupon = Coupon.builder()
                .member(member)
                .reason(reason)
                .discountRate(discountRate)
                .isUsed(false)
                .usedAt(LocalDateTime.now())
                .build();

        couponRepository.save(coupon);
    }
    
    public void PopCoupon(Long couponId) {
        Coupon coupon = couponRepository.findById(couponId)
                .orElseThrow(() -> new IllegalArgumentException("쿠폰을 찾을 수 없습니다."));

        coupon.useCoupon();
        couponRepository.save(coupon);
    }
    
    public List<Coupon> GetCouponByMember(Member member) {
    	return couponRepository.findByMember(member);
    }
    
    public int GetUsableCouponCount(Member member) {
    	return couponRepository.countByMemberAndIsUsedFalse(member);
    }
}
