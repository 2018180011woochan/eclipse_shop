package com.example.shop_project_v2.admin.member;

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
@RequestMapping("/admin/members")
@RequiredArgsConstructor
public class AdminMemberViewController {
	 private final MemberRepository memberRepository;
	
	@GetMapping
    public String getMembers(Model model) {
		// 회원 수 계산
        long userCount = memberRepository.countByRole(Role.USER);
        long adminCount = memberRepository.countByRole(Role.ADMIN);

        // 모든 회원 목록 조회
        List<Member> members = memberRepository.findAll();

        model.addAttribute("userCount", userCount);
        model.addAttribute("adminCount", adminCount);
        model.addAttribute("members", members);
        
        return "admin/member/memberListPage"; 
    }
}
