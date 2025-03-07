package com.example.shop_project_v2.comment.entity;

import java.time.LocalDateTime;

import com.example.shop_project_v2.BaseEntity;
import com.example.shop_project_v2.Inquiry.InquiryType;
import com.example.shop_project_v2.Inquiry.entity.Inquiry;
import com.example.shop_project_v2.member.entity.Member;
import com.example.shop_project_v2.product.entity.Product;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class Comment extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "inquiry_id", nullable = false)
    private Inquiry inquiry; // 문의

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member; // 작성자

    @Column(nullable = false)
    @Size(max = 500, message = "문의 답변은 500자 이내로 작성해주세요.")
    private String content; // 댓글 내용

}