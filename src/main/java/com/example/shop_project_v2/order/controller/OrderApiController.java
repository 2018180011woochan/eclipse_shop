package com.example.shop_project_v2.order.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.shop_project_v2.coupon.service.CouponService;
import com.example.shop_project_v2.member.entity.Member;
import com.example.shop_project_v2.member.repository.MemberRepository;
import com.example.shop_project_v2.member.service.MemberService;
import com.example.shop_project_v2.order.dto.OrderItemRequestDto;
import com.example.shop_project_v2.order.dto.OrderRequestDto;
import com.example.shop_project_v2.order.entity.Order;
import com.example.shop_project_v2.order.entity.OrderItem;
import com.example.shop_project_v2.order.service.OrderService;
import com.example.shop_project_v2.point.entity.Point;
import com.example.shop_project_v2.point.entity.UsedPoint;
import com.example.shop_project_v2.point.repository.PointRepository;
import com.example.shop_project_v2.point.repository.UsedPointRepository;
import com.example.shop_project_v2.point.service.PointService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
public class OrderApiController {

    private final OrderService orderService;
    private final MemberService memberService;
    private final PointService pointService;
    private final CouponService couponService;
    private final UsedPointRepository usedPointRepository;
    private final PointRepository pointRepository;
    
    
    @PostMapping
    public ResponseEntity<?> createOrder(@RequestBody OrderRequestDto orderRequest) {	
        Member member = memberService.getCurrentMember();
        Point point = pointService.findByMember(member)
            .orElseThrow(() -> new IllegalArgumentException("포인트 정보가 없습니다."));

        // 사용 포인트 차감
        int usedPoints = orderRequest.getUsedPoint();
        if (usedPoints > point.getBalance()) {
            throw new IllegalArgumentException("사용 포인트가 보유 포인트를 초과했습니다.");
        }
        
        // 쿠폰 적용
        if (orderRequest.getCouponId() != null) {
        	System.out.println("쿠폰사용!!!" + orderRequest.getCouponId());
            couponService.PopCoupon(orderRequest.getCouponId()); 
        }
        else
        	System.out.println("쿠폰사용안됨!!!" + orderRequest.getCouponId());
    	
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

        


        
        // UsedPoint 생성
        UsedPoint usedPoint = new UsedPoint();
        usedPoint.setPoint(point);
        usedPoint.setUsedPoint(usedPoints);
        usedPoint.setUsedReason("상품 주문");
        usedPoint.setOrder(order);
        usedPointRepository.save(usedPoint);

        // 포인트 잔액 차감
        point.usePoint(usedPoint);
        pointRepository.save(point);
        
        System.out.println("사용 포인트: " + usedPoints);
        System.out.println("UsedPoint 객체: " + usedPoint);
        
        // 3) 응답(JSON)
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("orderId", savedOrder.getOrderId());
        responseBody.put("totalPrice", savedOrder.getTotalPrice());
        return ResponseEntity.ok(responseBody);
    }
}
