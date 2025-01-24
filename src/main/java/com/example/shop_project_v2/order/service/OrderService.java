package com.example.shop_project_v2.order.service;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.shop_project_v2.order.entity.Order;
import com.example.shop_project_v2.order.entity.OrderItem;
import com.example.shop_project_v2.order.repository.OrderRepository;
import com.example.shop_project_v2.product.entity.Product;
import com.example.shop_project_v2.product.repository.ProductRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderService {
	private final OrderRepository orderRepository;
	private final ProductRepository productRepository;
	
    @Transactional
    public Order createOrder(Order order) {
        // 1) OrderItem들의 totalPrice 계산
        for (OrderItem item : order.getOrderItems()) {
            item.calculatePrice();
        }
        // 2) Order totalPrice 계산
        order.calculateTotalPrice();
        // 3) DB 저장
        return orderRepository.save(order);
    }
    
    public Map<LocalDate, List<Order>> getOrdersByDate(Long memberId) {
        List<Order> orders = orderRepository.findByMemberIdOrderByCreatedDateDesc(memberId);

        // 1) 날짜별로 groupBy
        Map<LocalDate, List<Order>> map = orders.stream()
            .collect(Collectors.groupingBy(
                o -> o.getCreatedDate().toLocalDate(),
                TreeMap::new, // 날짜 역/오름차순 정렬 가능
                Collectors.toList()
            ));

        // 2) 각 Order 마다 orderItems 정렬 (productId 기준)
        //    (이후 Thymeleaf에서 group-break에 사용)
        for (List<Order> dayOrders : map.values()) {
            for (Order o : dayOrders) {
                // 오름차순 정렬
                o.getOrderItems().sort(Comparator.comparing(OrderItem::getProductId));
                // (선택) 썸네일이 필요하면 아래처럼 미리 채워 넣을 수도 있음:
                for (OrderItem oi : o.getOrderItems()) {
                    if (oi.getThumbnailUrl() == null) {
                        productRepository.findById(oi.getProductId())
                            .ifPresent(p -> oi.setThumbnailUrl(p.getThumbnailUrl()));
                    }
                }
            }
        }

        return map;
    }
}
