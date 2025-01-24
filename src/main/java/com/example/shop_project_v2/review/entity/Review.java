package com.example.shop_project_v2.review.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.example.shop_project_v2.BaseEntity;
import com.example.shop_project_v2.member.entity.Member;
import com.example.shop_project_v2.order.entity.OrderItem;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Review extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id")
    private Long reviewId;

    @Column(nullable = false)
    private int stars;

    @Column(nullable = false, length = 500)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member; // 리뷰 작성자

    @OneToOne
    @JoinColumn(name = "order_detail_id", unique = true, nullable = false)
    private OrderItem orderItem; // 리뷰 상품

    @Column(name = "product_id", nullable = false)
    private Long productId;

}
