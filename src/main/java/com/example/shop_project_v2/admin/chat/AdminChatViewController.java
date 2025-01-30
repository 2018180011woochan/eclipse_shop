package com.example.shop_project_v2.admin.chat;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.shop_project_v2.member.Role;
import com.example.shop_project_v2.member.entity.Member;
import com.example.shop_project_v2.member.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/admin/chat")
@RequiredArgsConstructor
public class AdminChatViewController {
	 private final MemberRepository memberRepository;
	
	@GetMapping
    public String chatPage(Model model) {

        
        return "admin/chat/adminChat"; // 회원 관리 페이지
    }
}
