package com.example.shop_project_v2.cart.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/cart")
public class CartViewController {
    @GetMapping
    public String viewCart() {
        return "cart/cart";
    }
}