package com.example.shop_project_v2.order.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.shop_project_v2.member.entity.Member;
import com.example.shop_project_v2.member.service.MemberService;
import com.example.shop_project_v2.order.entity.Order;
import com.example.shop_project_v2.order.entity.OrderItem;
import com.example.shop_project_v2.order.service.OrderService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/order")
public class OrderViewController {
	private final MemberService memberService;
	private final OrderService orderService;
	
	
	
    @GetMapping
    public String showOrderPage() {
        return "order/order";
    }
    
    @GetMapping("/history")
    public String orderHistory(Model model) {
        Long memberId = memberService.getCurrentMember().getId(); 

        // 날짜별로 Orders
        Map<LocalDate, List<Order>> ordersByDate = orderService.getOrdersByDate(memberId);

        model.addAttribute("ordersByDate", ordersByDate);
        return "order/orderHistory"; 
    }
   
    
}
