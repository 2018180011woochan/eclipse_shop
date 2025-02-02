package com.example.shop_project_v2.order.service;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.shop_project_v2.member.entity.Member;
import com.example.shop_project_v2.member.repository.MemberRepository;
import com.example.shop_project_v2.order.OrderStatus;
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
	private final MemberRepository memberRepository;
	
    @Transactional
    public Order createOrder(Order order) {
        for (OrderItem item : order.getOrderItems()) {
            item.calculatePrice();
        }
        order.calculateTotalPrice();
        return orderRepository.save(order);
    }
    
    public List<Order> findAllOrders() {
        return orderRepository.findAll();
    }
    
    public Order findOrderById(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("주문을 찾을 수 없습니다."));
    }
    
    public Map<LocalDate, List<Order>> getOrdersByDate(Long memberId) {
        List<Order> orders = orderRepository.findByMemberIdOrderByCreatedDateDesc(memberId);

        Map<LocalDate, List<Order>> map = orders.stream()
            .collect(Collectors.groupingBy(
                o -> o.getCreatedDate().toLocalDate(),
                TreeMap::new, // 날짜 역/오름차순 정렬 가능
                Collectors.toList()
            ));

        for (List<Order> dayOrders : map.values()) {
            for (Order o : dayOrders) {
                // 오름차순 정렬
                o.getOrderItems().sort(Comparator.comparing(OrderItem::getProductId));
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
    
    public Integer getOrderCountByMember(Member member){
        return orderRepository.findAllByMember(member).size();
    }
    
    public void updateOrderStatus(Long orderId, OrderStatus orderStatus) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("주문을 찾을 수 없습니다."));
        order.updateStatus(orderStatus);
        orderRepository.save(order);
    }
}
