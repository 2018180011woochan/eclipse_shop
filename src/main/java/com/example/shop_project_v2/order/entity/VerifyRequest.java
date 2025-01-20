package com.example.shop_project_v2.order.entity;

import lombok.Data;

@Data
public class VerifyRequest {
 private String impUid;
 private String merchantUid;
}