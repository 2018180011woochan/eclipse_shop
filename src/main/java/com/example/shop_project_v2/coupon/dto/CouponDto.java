package com.example.shop_project_v2.coupon.dto;

import java.time.LocalDateTime;

import com.example.shop_project_v2.coupon.entity.Coupon;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CouponDto {
	private Long id;
    private String reason;
    private int discountRate;
    private boolean isUsed;
    private LocalDateTime createdDate;

    public CouponDto(Coupon coupon) {
        this.id = coupon.getId();
        this.reason = coupon.getReason();
        this.discountRate = coupon.getDiscountRate();
        this.isUsed = coupon.isUsed();
        this.createdDate = coupon.getCreatedDate();
    }
}
