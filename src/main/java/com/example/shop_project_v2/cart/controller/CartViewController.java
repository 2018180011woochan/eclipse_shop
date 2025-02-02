package com.example.shop_project_v2.cart.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.shop_project_v2.Inquiry.service.InquiryService;
import com.example.shop_project_v2.coupon.service.CouponService;
import com.example.shop_project_v2.member.entity.Member;
import com.example.shop_project_v2.member.repository.MemberRepository;
import com.example.shop_project_v2.member.service.MemberService;
import com.example.shop_project_v2.order.service.OrderService;
import com.example.shop_project_v2.point.service.PointService;
import com.example.shop_project_v2.review.service.ReviewService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/cart")
public class CartViewController {
	private final MemberService memberService;
    @GetMapping
    public String viewCart(Model model) {
        String membershipName = "";
        
        try {
            membershipName = memberService.getCurrentMember().getMembership().getName();
        } catch (RuntimeException ex) {
            membershipName = "BRONZE"; // 비로그인 상태 -> 할인 없음
        }
    	
    	model.addAttribute("membershipStr", membershipName);
        return "cart/cart";
    }
}