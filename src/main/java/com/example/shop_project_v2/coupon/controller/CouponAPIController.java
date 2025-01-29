package com.example.shop_project_v2.coupon.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.shop_project_v2.coupon.dto.CouponDto;
import com.example.shop_project_v2.coupon.service.CouponService;
import com.example.shop_project_v2.member.entity.Member;
import com.example.shop_project_v2.member.service.MemberService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/coupons")
@RequiredArgsConstructor
public class CouponAPIController {
    private final CouponService couponService;
    private final MemberService memberService;
    
    @GetMapping
    public List<CouponDto> getMyCoupons() {
        Member currentMember = memberService.getCurrentMember();
        return couponService.GetCouponByMember(currentMember)
                .stream()
                .map(CouponDto::new) 
                .collect(Collectors.toList());
    }
}
