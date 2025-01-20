package com.example.shop_project_v2.order.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.shop_project_v2.order.entity.PaymentData;

@Service
public class PortOneService {
    private final RestTemplate restTemplate = new RestTemplate();

    // 포트원 관리자 콘솔에서 발급받은 키/시크릿
    private final String API_KEY = "YOUR_REST_API_KEY";
    private final String API_SECRET = "YOUR_REST_API_SECRET";

    // 1) 토큰 발급
    public String getAccessToken() {
        String url = "https://api.iamport.kr/users/getToken";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, String> body = new HashMap<>();
        body.put("imp_key", API_KEY);
        body.put("imp_secret", API_SECRET);

        HttpEntity<Map<String, String>> request = new HttpEntity<>(body, headers);

        ResponseEntity<Map> response =
            restTemplate.postForEntity(url, request, Map.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            Map<String, Object> responseBody = response.getBody();
            Map<String, Object> data = (Map<String, Object>) responseBody.get("response");
            String token = (String) data.get("access_token");
            return token;
        } else {
            throw new RuntimeException("토큰 발급 실패");
        }
    }
    

    public PaymentData getPaymentData(String impUid) {
        // 1) 토큰 얻기
        String accessToken = getAccessToken();

        // 2) 결제 정보 조회
        String url = "https://api.iamport.kr/payments/" + impUid;

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken); // "Authorization: Bearer {토큰}"
        
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<Map> response = restTemplate.exchange(
            url,
            HttpMethod.GET,
            entity,
            Map.class
        );

        if (response.getStatusCode() == HttpStatus.OK) {
            Map<String, Object> body = response.getBody();
            Map<String, Object> data = (Map<String, Object>) body.get("response");
            // 실제 결제 정보 파싱
            PaymentData paymentData = new PaymentData();
            paymentData.setImpUid((String) data.get("imp_uid"));
            paymentData.setMerchantUid((String) data.get("merchant_uid"));
            paymentData.setAmount(((Number) data.get("amount")).intValue());
            paymentData.setStatus((String) data.get("status")); // "paid", "ready", etc
            return paymentData;
        } else {
            throw new RuntimeException("결제 정보 조회 실패");
        }
    }

}

