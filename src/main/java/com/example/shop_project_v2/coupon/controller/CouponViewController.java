package com.example.shop_project_v2.coupon.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.shop_project_v2.coupon.service.CouponService;
import com.example.shop_project_v2.member.service.MemberService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/coupon")
@RequiredArgsConstructor
public class CouponViewController {
	private final CouponService couponService;
	private final MemberService memberService;
	
	@GetMapping("/history")
	public String CouponList(Model model) {
		model.addAttribute("coupons", couponService.GetCouponByMember(memberService.getCurrentMember()));
		return "coupon/couponHistory";
	}
}
