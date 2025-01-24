package com.example.shop_project_v2.review.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.shop_project_v2.member.entity.Member;
import com.example.shop_project_v2.member.service.MemberService;
import com.example.shop_project_v2.review.service.ReviewService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class ReviewViewController {
    private final ReviewService reviewService;
    private final MemberService memberService;

    @GetMapping("/review/write")
    public String reviewWriteForm(
            @RequestParam("orderItemId") Long orderItemId,
            @RequestParam("productId") Long productId,
            Model model) {

        // 필요하다면, orderItem 또는 상품 정보를 추가 조회하여
        // 리뷰 페이지에 표시할 수도 있습니다.
        // 예: model.addAttribute("itemInfo", ...);

        model.addAttribute("orderItemId", orderItemId);
        model.addAttribute("productId", productId);

        return "review/reviewWrite"; 
    }

    @PostMapping("/review/save")
    public String saveReview(
            @RequestParam Long orderItemId,
            @RequestParam Long productId,
            @RequestParam int stars,
            @RequestParam String title,
            @RequestParam String content) {

        Member currentMember = memberService.getCurrentMember();

        reviewService.saveReview(orderItemId, productId, stars, title, content, currentMember);

        return "redirect:/"; 
    }
}
