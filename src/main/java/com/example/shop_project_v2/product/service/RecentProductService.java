package com.example.shop_project_v2.product.service;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

import java.util.stream.Collectors;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import com.example.shop_project_v2.product.entity.Product;
import com.example.shop_project_v2.product.repository.ProductRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RecentProductService {

    private final StringRedisTemplate redisTemplate;
    private final ProductRepository productRepository;

    public void saveRecentProduct(String redisKey, Long productId) {
        ListOperations<String, String> ops = redisTemplate.opsForList();
        // 기존에 있으면 제거
        ops.remove(redisKey, 0, productId.toString());
        // 앞에 추가
        ops.leftPush(redisKey, productId.toString());
        // 최대 5개 유지
        ops.trim(redisKey, 0, 4);
    }

    public List<Product> getRecentProducts(String redisKey) {
        ListOperations<String, String> ops = redisTemplate.opsForList();
        List<String> idStrings = ops.range(redisKey, 0, 4);

        if (idStrings == null || idStrings.isEmpty()) {
            return Collections.emptyList();
        }

        // Long ID 리스트로 변환
        List<Long> ids = idStrings.stream()
                                  .map(Long::valueOf)
                                  .collect(Collectors.toList());

        // DB 에서 상품 조회
        List<Product> products = productRepository.findAllById(ids);

        // Redis 순서대로 정렬
        Map<Long, Integer> order = new HashMap<>();
        for (int i = 0; i < ids.size(); i++) {
            order.put(ids.get(i), i);
        }
        products.sort(Comparator.comparingInt(p -> order.get(p.getProductId())));

        return products;
    }
}


