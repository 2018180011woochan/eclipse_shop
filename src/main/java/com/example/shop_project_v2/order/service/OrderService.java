package com.example.shop_project_v2.order.service;

import org.springframework.stereotype.Service;

import com.example.shop_project_v2.order.entity.Order;
import com.example.shop_project_v2.order.entity.OrderItem;
import com.example.shop_project_v2.order.repository.OrderRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderService {
	private final OrderRepository orderRepository;

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
}
