package com.example.shop_project_v2.coupon.entity;

import java.time.LocalDateTime;

import com.example.shop_project_v2.BaseEntity;
import com.example.shop_project_v2.member.entity.Member;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Coupon extends BaseEntity  {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; 

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member; 

    private String reason; 

    private int discountRate; 

    private boolean isUsed; 

    private LocalDateTime usedAt; 

    // 쿠폰 사용 메서드
    public void useCoupon() {
        if (!this.isUsed) {
            this.isUsed = true;
            this.usedAt = LocalDateTime.now();
        } else {
            throw new IllegalStateException("이미 사용된 쿠폰입니다.");
        }
    }
}
