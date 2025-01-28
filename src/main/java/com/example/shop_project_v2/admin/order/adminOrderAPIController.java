package com.example.shop_project_v2.admin.order;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.shop_project_v2.order.OrderStatus;
import com.example.shop_project_v2.order.service.OrderService;

import lombok.RequiredArgsConstructor;

@RequestMapping("/admin/api/orders")
@RequiredArgsConstructor
@RestController
public class AdminOrderAPIController {
	private final OrderService orderService;
	
	@PostMapping("/{orderId}/update-status")
	public ResponseEntity<Map<String, String>> updateOrderStatus(
	    @PathVariable Long orderId,
	    @RequestParam OrderStatus orderStatus) {
        System.out.println("여기는 컨트롤러");
	    orderService.updateOrderStatus(orderId, orderStatus); // 주문 상태 업데이트

	    Map<String, String> response = new HashMap<>();
	    response.put("message", "주문 상태가 업데이트되었습니다.");
	    response.put("newStatus", orderStatus.name());

	    return ResponseEntity.ok(response); // JSON 응답 반환
	}
}
