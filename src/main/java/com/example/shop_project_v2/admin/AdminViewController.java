package com.example.shop_project_v2.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/admin")
@Controller
public class AdminViewController {
	@GetMapping
	public String mainPage() {
		return "admin/adminMainPage";
	}
}
