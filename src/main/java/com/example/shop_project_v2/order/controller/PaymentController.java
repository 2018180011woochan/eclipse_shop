package com.example.shop_project_v2.order.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.shop_project_v2.order.entity.PaymentData;
import com.example.shop_project_v2.order.entity.VerifyRequest;
import com.example.shop_project_v2.order.service.PortOneService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class PaymentController {
    private final PortOneService portOneService;

    @PostMapping("/api/payments/verify")
    public ResponseEntity<?> verifyPayment(@RequestBody VerifyRequest request) {
        try {
            PaymentData paymentData = portOneService.getPaymentData(request.getImpUid());

            if (!paymentData.getMerchantUid().equals(request.getMerchantUid())) {
                return ResponseEntity.badRequest().body("잘못된 주문번호");
            }
            if ("paid".equals(paymentData.getStatus())) {
                // 주문 상태를 '결제완료'로 업데이트 등 처리
                return ResponseEntity.ok(Map.of("status", "success"));
            } else {
                return ResponseEntity.badRequest().body("결제되지 않은 상태");
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("결제 검증 실패: " + e.getMessage());
        }
    }
}



