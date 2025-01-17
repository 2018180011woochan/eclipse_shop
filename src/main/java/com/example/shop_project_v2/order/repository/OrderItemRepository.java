package com.example.shop_project_v2.order.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.shop_project_v2.order.entity.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}
