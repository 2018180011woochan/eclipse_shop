package com.example.shop_project_v2.admin.order;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.shop_project_v2.order.entity.Order;
import com.example.shop_project_v2.order.service.OrderService;

import lombok.RequiredArgsConstructor;

@RequestMapping("/admin/orders")
@RequiredArgsConstructor
@Controller
public class AdminOrderViewController {
	private final OrderService orderService;
	
	@GetMapping()
	public String orderPage(Model model) {
        List<Order> orders = orderService.findAllOrders(); 
        model.addAttribute("orders", orders);
		return "admin/order/orderPage.html";
	}
	
    @GetMapping("/{orderId}")
    public String orderDetailPage(@PathVariable Long orderId, Model model) {
        Order order = orderService.findOrderById(orderId); // 주문 상세 정보 조회
        model.addAttribute("order", order); // 모델에 주문 정보 추가
        return "admin/order/orderDetailPage.html";
    }
}
