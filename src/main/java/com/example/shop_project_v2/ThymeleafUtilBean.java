package com.example.shop_project_v2;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.shop_project_v2.order.entity.OrderItem;
import com.example.shop_project_v2.product.entity.Product;
import com.example.shop_project_v2.product.repository.ProductRepository;

@Component
public class ThymeleafUtilBean {
    
    // Map<Long, List<OrderItem>>를 만드는 메서드
    public Map<Long, List<OrderItem>> groupItemsByProduct(List<OrderItem> orderItems) {
        return orderItems.stream()
                .collect(Collectors.groupingBy(OrderItem::getProductId));
    }

    // DB에서 Product 썸네일 조회(스냅샷 없는 경우)
    @Autowired
    private ProductRepository productRepo;

    public String findThumbnailUrl(Long productId) {
        return productRepo.findById(productId)
                .map(Product::getThumbnailUrl)
                .orElse("/images/no_image.png");
    }
}
