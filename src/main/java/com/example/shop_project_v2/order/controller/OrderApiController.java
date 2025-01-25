package com.example.shop_project_v2.order.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.shop_project_v2.member.repository.MemberRepository;
import com.example.shop_project_v2.member.service.MemberService;
import com.example.shop_project_v2.order.dto.OrderItemRequestDto;
import com.example.shop_project_v2.order.dto.OrderRequestDto;
import com.example.shop_project_v2.order.entity.Order;
import com.example.shop_project_v2.order.entity.OrderItem;
import com.example.shop_project_v2.order.service.OrderService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
public class OrderApiController {

    private final OrderService orderService;
    private final MemberService memberService;
    
    @PostMapping
    public ResponseEntity<?> createOrder(@RequestBody OrderRequestDto orderRequest) {
        // 1) DTO → 엔티티 변환
        Order order = new Order();
        order.setMember(memberService.getCurrentMember());
        order.setAddress(orderRequest.getAddress());
        order.setPaymentMethod(orderRequest.getPaymentMethod());

        for (OrderItemRequestDto itemDto : orderRequest.getOrderItems()) {
            OrderItem orderItem = new OrderItem();
            orderItem.setProductId(itemDto.getProductId());
            orderItem.setProductName(itemDto.getProductName());
            orderItem.setColor(itemDto.getColor());
            orderItem.setSize(itemDto.getSize());
            orderItem.setQuantity(itemDto.getQuantity());
            orderItem.setUnitPrice(itemDto.getUnitPrice());
            orderItem.calculatePrice();
            orderItem.setIsReview(false);

            order.addOrderItem(orderItem);
        }

        // 2) 주문 생성
        Order savedOrder = orderService.createOrder(order);

        // 3) 응답(JSON)
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("orderId", savedOrder.getOrderId());
        responseBody.put("totalPrice", savedOrder.getTotalPrice());
        return ResponseEntity.ok(responseBody);
    }
}
