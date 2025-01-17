package com.example.shop_project_v2.order.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/order")
public class OrderViewController {

    @GetMapping
    public String showOrderPage() {
        // templates/order.html 뷰로 이동
        return "order/order";
    }
}
