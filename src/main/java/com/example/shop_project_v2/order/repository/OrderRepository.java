package com.example.shop_project_v2.order.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.shop_project_v2.order.entity.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {
	List<Order> findByMemberIdOrderByCreatedDateDesc(Long memberId);
}


