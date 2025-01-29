package com.example.shop_project_v2.order.dto;

import java.util.List;

import lombok.Data;

@Data
public class OrderRequestDto {
    private Long memberId;
    private String address;
    private String paymentMethod;
    private List<OrderItemRequestDto> orderItems;
    private int usedPoint;
    private Long couponId;
}
