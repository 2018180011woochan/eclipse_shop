package com.example.shop_project_v2.order.entity;

import lombok.Data;

@Data
public class PaymentData {
    private String impUid;
    private String merchantUid;
    private int amount;
    private String status; 
}

