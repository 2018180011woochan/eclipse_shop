package com.example.shop_project_v2;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    @GetMapping("/")
    public String home() {
        return "home"; // 뷰 템플릿: resources/templates/home.html
    }
}
