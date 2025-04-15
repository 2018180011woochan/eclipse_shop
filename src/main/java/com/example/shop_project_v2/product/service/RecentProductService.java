package com.example.shop_project_v2.product.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RecentProductService {

    private final StringRedisTemplate redisTemplate;
    
    // 최근 본 상품 저장
    public void saveRecentProduct(String redisKey, Long productId) {
        ListOperations<String, String> listOps = redisTemplate.opsForList();
        // 이미 있으면 제거하기
        listOps.remove(redisKey, 0, productId.toString());
        // 다시 푸시 (최신순으로)
        listOps.leftPush(redisKey, productId.toString());
        // 최근 5개까지만 저장
        listOps.trim(redisKey, 0, 4);
    }
    
    // 최근 본 상품 ID 목록 조회
    public List<Long> getRecentProductIds(String redisKey) {
        ListOperations<String, String> listOps = redisTemplate.opsForList();
        List<String> ids = listOps.range(redisKey, 0, 4);
        
        if (ids == null) {
            return new ArrayList<>();
        }
        return ids.stream()
                .map(Long::valueOf)
                .collect(Collectors.toList());
    }
}
