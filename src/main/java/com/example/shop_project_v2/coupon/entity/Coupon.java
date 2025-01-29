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
    private Long id; // 쿠폰 ID

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member; // 쿠폰을 소유한 회원 (1:N 관계)

    private String reason; // 지급 이유

    private int discountRate; // 할인율 (%)

    private boolean isUsed; // 사용 여부

    private LocalDateTime usedAt; // 사용 날짜

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
