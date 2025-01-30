package com.example.shop_project_v2.chatbot.service;

import org.springframework.stereotype.Service;

@Service
public class ChatbotService {
    public String getResponse(String message) {
        if(message == null) return "무슨 말씀이신지 잘 모르겠습니다.";

        message = message.trim().toLowerCase();
        switch (message) {
        case "배송 기간은 얼마나 걸리나요?":
            return "보통 주문 후 2~5일 내에 배송됩니다.";
        case "배송 추적은 어디서 할 수 있나요?":
            return "마이페이지에서 주문내역을 확인해주세요.";
        case "주문 취소는 어떻게 하나요?":
            return "마이페이지에서 주문취소 요청을 하거나 고객센터로 문의해주세요.";
        case "결제 가능한 방법은?":
            return "신용카드, 카카오페이, 네이버페이 등 다양한 방법을 지원합니다.";
        case "환불 및 교환 정책?":
            return "상품 수령 후 7일 이내 교환 및 환불이 가능합니다.";
        case "상담사 연결":
        	return "상담사를 연결 중입니다 잠시만 기다려주세요.";
        	
        default:
            return "해당 질문의 답변은 준비중입니다 상담사에게 문의하세요.";
    }
    }
}
