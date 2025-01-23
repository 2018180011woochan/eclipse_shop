package com.example.shop_project_v2.Inquiry.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.example.shop_project_v2.BaseEntity;
import com.example.shop_project_v2.Inquiry.InquiryType;
import com.example.shop_project_v2.member.Membership;
import com.example.shop_project_v2.member.Role;
import com.example.shop_project_v2.member.entity.Member;
import com.example.shop_project_v2.product.entity.Product;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "inquiries")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Inquiry extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 문의 고유 ID

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Column(nullable = false, length = 50)
    @NotBlank(message = "문의 제목은 필수입니다.")
    @Size(max = 50, message = "문의 제목은 50자 이내로 작성해주세요.")
    private String title; // 문의 제목

    @Column(nullable = false, columnDefinition = "TEXT")
    @NotBlank(message = "문의 내용은 필수입니다.")
    @Size(max = 500, message = "문의 제목은 500자 이내로 작성해주세요.")
    private String content; // 문의 내용

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private InquiryType type; // 문의 유형 (SIZE, SHIPPING, RESTOCK, DETAILS)

    @Column(nullable = false)
    private boolean isSecret; // 비밀글 여부

    //@OneToMany(mappedBy = "inquiry", cascade = CascadeType.ALL, orphanRemoval = true)
    //private List<Comment> comments;

    @Transient
    private int commentCount; // 댓글 개수
}
