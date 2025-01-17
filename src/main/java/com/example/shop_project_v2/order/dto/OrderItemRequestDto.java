package com.example.shop_project_v2.order.dto;

import lombok.Data;

@Data
public class OrderItemRequestDto {
    private Long productId;
    private String productName;
    private String color;
    private String size;
    private int quantity;
    private int unitPrice;
}
