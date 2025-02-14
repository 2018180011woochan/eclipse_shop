package com.example.shop_project_v2.coupon.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.example.shop_project_v2.coupon.entity.Coupon;
import com.example.shop_project_v2.coupon.repository.CouponRepository;
import com.example.shop_project_v2.member.Membership;
import com.example.shop_project_v2.member.entity.Member;
import com.example.shop_project_v2.member.repository.MemberRepository;

import jakarta.transaction.Transactional;
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
    
    @Scheduled(cron = "0 0 0 1 * ?")
    @Transactional
    public void PushMonthlyCoupon() {
    	List<Member> members = memberRepository.findAll();
    	
    	for (Member member : members) {
    		int discountRate = getDiscountByMembership(member.getMembership());
    		
            if (discountRate > 0 && !hasAlreadyIssuedCoupon(member)) {
                Coupon coupon = Coupon.builder()
                        .member(member)
                        .reason(member.getMembership().getName() + " 등급 월간 쿠폰")
                        .discountRate(discountRate)
                        .isUsed(false)
                        .createdDate(LocalDateTime.now())
                        .build();

                couponRepository.save(coupon);
            }
    	}
    }
    
    // 등급별 할인율 반환
    private int getDiscountByMembership(Membership membership) {
        switch (membership) {
            case BRONZE: return 5;
            case SILVER: return 10;
            case GOLD: return 15;
            case DIAMOND: return 20;
            default: return 0;
        }
    }

    // 이미 해당 월에 지급된 쿠폰이 있는지 확인
    private boolean hasAlreadyIssuedCoupon(Member member) {
        LocalDateTime startOfMonth = LocalDateTime.now().withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0);
        return couponRepository.existsByMemberAndCreatedDateAfter(member, startOfMonth);
    }
}
