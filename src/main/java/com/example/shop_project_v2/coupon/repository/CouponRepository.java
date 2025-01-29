package com.example.shop_project_v2.coupon.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.shop_project_v2.coupon.entity.Coupon;
import com.example.shop_project_v2.member.entity.Member;


public interface CouponRepository extends JpaRepository<Coupon, Long> {
	List<Coupon> findByMember(Member member);
    int countByMemberAndIsUsedFalse(Member member);
    boolean existsByMemberAndCreatedDateAfter(Member member, LocalDateTime startOfMonth);
}
