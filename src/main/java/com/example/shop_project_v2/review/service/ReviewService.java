package com.example.shop_project_v2.review.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.shop_project_v2.Inquiry.entity.Inquiry;
import com.example.shop_project_v2.member.entity.Member;
import com.example.shop_project_v2.member.repository.MemberRepository;
import com.example.shop_project_v2.order.entity.OrderItem;
import com.example.shop_project_v2.order.repository.OrderItemRepository;
import com.example.shop_project_v2.review.entity.Review;
import com.example.shop_project_v2.review.repository.ReviewRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReviewService {
	private final MemberRepository memberRepository;
    private final ReviewRepository reviewRepository;
    private final OrderItemRepository orderItemRepository;

    @Transactional
    public void saveReview(Long orderItemId,
                           Long productId,
                           int stars,
                           String title,
                           String content,
                           Member member) {

        // OrderItem 조회
        OrderItem orderItem = orderItemRepository.findById(orderItemId)
                .orElseThrow(() -> new IllegalArgumentException("OrderItem not found"));

        if (Boolean.TRUE.equals(orderItem.getIsReview())) {
            throw new IllegalStateException("이미 리뷰가 작성된 상품입니다.");
        }

        // Review 엔티티 생성
        Review review = Review.builder()
                .stars(stars)
                .content(content)
                .member(member)
                .productId(productId)
                .orderItem(orderItem)
                // .title(title) // title 필드가 Review 엔티티에 있다면
                .build();

        reviewRepository.save(review);

        orderItem.setIsReview(true);
    }
    
    public List<Review> findReviewsByMember(Member member) {
        return reviewRepository.findByMember(member);
    }
    
    public List<Review> getReviewByProduct(Long productId) {
        return reviewRepository.findByProductId(productId);
    }

    public Review getReviewById(Long reviewId) {
        return reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("리뷰가 존재하지 않습니다."));
    }
    
    public Integer getReviewCountByMember(Member member){
        return reviewRepository.findByMember(member).size();
    }
}

