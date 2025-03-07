package com.example.shop_project_v2.review.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.shop_project_v2.Inquiry.dto.InquiryRequestDto;
import com.example.shop_project_v2.Inquiry.entity.Inquiry;
import com.example.shop_project_v2.member.entity.Member;
import com.example.shop_project_v2.member.service.MemberService;
import com.example.shop_project_v2.order.entity.Order;
import com.example.shop_project_v2.review.entity.Review;
import com.example.shop_project_v2.review.service.ReviewService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/review")
@RequiredArgsConstructor
public class ReviewViewController {
    private final ReviewService reviewService;
    private final MemberService memberService;

    @GetMapping("/write")
    public String reviewWriteForm(
            @RequestParam("orderItemId") Long orderItemId,
            @RequestParam("productId") Long productId,
            Model model) {
    	
        model.addAttribute("orderItemId", orderItemId);
        model.addAttribute("productId", productId);

        return "review/reviewWrite"; 
    }

    @PostMapping("/save")
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
    
    @GetMapping("/history")
    public String reviewHistory(Model model) {
    	Member currentMember = memberService.getCurrentMember();
    	List<Review> reviews = reviewService.findReviewsByMember(currentMember);
    	model.addAttribute("reviews", reviews);
    	return "review/reviewHistory";
    }
    
    @GetMapping
    public String listByReview(@RequestParam Long productId, Model model) {
    	List<Review> reviews = reviewService.getReviewByProduct(productId);
    	Double averageRating = reviewService.getAverageRating(productId);
        model.addAttribute("reviews", reviews);
        model.addAttribute("averageRating", averageRating);
        model.addAttribute("productId", productId);
        return "review/reviewList"; 
    }
    
    @GetMapping("/{reviewId}")
    public String detail(@PathVariable Long reviewId, Model model) {
    	Review review = reviewService.getReviewById(reviewId);
    	
        model.addAttribute("review", review);
        return "review/reviewDetail";
    }
    


}
